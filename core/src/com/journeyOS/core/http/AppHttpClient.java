package com.journeyOS.core.http;

import androidx.annotation.NonNull;

import com.journeyOS.base.network.NetWork;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.FileUtil;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.config.GithubConfig;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AppHttpClient {
    private final static String TAG = AppHttpClient.class.getSimpleName();
    private final static int HTTP_TIME_OUT = 16 * 1000;
    private final static int MAX_CACHE_SIZE = 8 * 1024 * 1024;
    private final static int CACHE_MAX_AGE = 4 * 7 * 24 * 60 * 60;

    private Map<String, Object> serviceByType = new HashMap<>();
    private Retrofit mRetrofit;


    private AppHttpClient(String baseUrl, String token) {
        Cache cache = new Cache(FileUtil.getHttpImageCacheDir(CoreManager.getContext()),
                MAX_CACHE_SIZE);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new BaseInterceptor(token))
                .addNetworkInterceptor(new NetworkBaseInterceptor())
                .cache(cache)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    public static AppHttpClient getInstance(String token) {
        return new AppHttpClient(GithubConfig.GITHUB_API_BASE_URL, token);
    }

    public static AppHttpClient getInstance(String baseUrl, String token) {
        return new AppHttpClient(baseUrl, token);
    }

    public synchronized <T> T getService(Class<T> apiInterface) {
        String serviceName = apiInterface.getName();
        if (BaseUtils.isNull(serviceByType.get(serviceName))) {
            T service = mRetrofit.create(apiInterface);
            serviceByType.put(serviceName, service);
            return service;
        } else {
            return (T) serviceByType.get(serviceName);
        }
    }

    private class BaseInterceptor implements Interceptor {
        String token = null;

        public BaseInterceptor(String token) {
            this.token = token;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();

            //add access token
            if (!BaseUtils.isNull(token)) {
                HttpUrl url = request.url().newBuilder()
//                        .addQueryParameter("uniqueLoginId", CoreManager.getAuthUser().loginId)
                        .build();
                String auth = token.startsWith("Basic") ? token : "token " + token;
                request = request.newBuilder()
                        .addHeader("Authorization", auth)
                        .url(url)
                        .build();
                LogUtils.d(TAG, "url = " + url);
            }

            //第二次请求，强制使用网络请求
            String forceNetWork = request.header("forceNetWork");
            if ("true".equals(forceNetWork)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            }
            //有forceNetWork且无网络状态下取从缓存中取
            else if (!BaseUtils.isNull(forceNetWork) &&
                    !NetWork.isAvailable(CoreManager.getContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            return chain.proceed(request);
        }
    }

    /**
     * 网络请求拦截器
     */
    private class NetworkBaseInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            Request request = chain.request();
            Response originalResponse = chain.proceed(request);

            String requestCacheControl = request.cacheControl().toString();

            //有forceNetWork时，强制更改缓存策略
            String forceNetWork = request.header("forceNetWork");
            if (!BaseUtils.isNull(forceNetWork)) {
                requestCacheControl = getCacheString();
            }

            if (BaseUtils.isNull(requestCacheControl)) {
                return originalResponse;
            } else {//设置缓存策略
                Response res = originalResponse.newBuilder()
                        .header("Cache-Control", requestCacheControl)
                        .removeHeader("Pragma")
                        .build();
                return res;
            }

        }
    }

    public static String getCacheString() {
        return "public, max-age=" + CACHE_MAX_AGE;
    }

}
