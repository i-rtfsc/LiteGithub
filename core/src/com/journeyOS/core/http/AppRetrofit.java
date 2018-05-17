/*
 * Copyright (c) 2018 anqi.huang@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.journeyOS.core.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.journeyOS.base.network.NetWork;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.FileUtil;
import com.journeyOS.core.CoreManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public enum AppRetrofit {
    INSTANCE;

    private final String TAG = AppRetrofit.class.getSimpleName();

    private final static int HTTP_TIME_OUT = 16 * 1000;

    private final static int MAX_CACHE_SIZE = 8 * 1024 * 1024;

    private final static int CACHE_MAX_AGE = 4 * 7 * 24 * 60 * 60;

    private HashMap<String, Retrofit> retrofitMap = new HashMap<>();
    private String token;

    private void createRetrofit(@NonNull String baseUrl) {
        int timeOut = HTTP_TIME_OUT;
        Cache cache = new Cache(FileUtil.getHttpImageCacheDir(CoreManager.getContext()),
                MAX_CACHE_SIZE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                .addInterceptor(new BaseInterceptor())
                .addNetworkInterceptor(new NetworkBaseInterceptor())
                .cache(cache)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        retrofitMap.put(baseUrl, retrofit);
    }

    public Retrofit getRetrofit(@NonNull String baseUrl, @Nullable String token) {
        this.token = token;
        if (!retrofitMap.containsKey(baseUrl)) {
            createRetrofit(baseUrl);
        }
        return retrofitMap.get(baseUrl);
    }

    /**
     * 拦截器
     */
    private class BaseInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();

            //add access token
            String url = request.url().toString();
            if (!BaseUtils.isNull(token)) {
                String auth = token.startsWith("Basic") ? token : "token " + token;
                request = request.newBuilder()
                        .addHeader("Authorization", auth)
                        .url(url)
                        .build();
            }
            Log.d(TAG, request.url().toString());

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
