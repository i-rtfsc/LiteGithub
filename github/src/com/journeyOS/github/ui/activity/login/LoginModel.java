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

package com.journeyOS.github.ui.activity.login;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.thread.ICoreExecutorsApi;
import com.journeyOS.core.api.userprovider.AuthUser;
import com.journeyOS.core.api.userprovider.IAuthUserProvider;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.config.GithubConfig;
import com.journeyOS.core.http.AppHttpClient;
import com.journeyOS.core.http.AuthRequestModel;
import com.journeyOS.core.http.HttpCoreManager;
import com.journeyOS.core.http.HttpObserver;
import com.journeyOS.core.http.HttpProgressSubscriber;
import com.journeyOS.core.http.HttpResponse;
import com.journeyOS.core.viewmodel.BaseViewModel;
import com.journeyOS.github.api.GithubService;
import com.journeyOS.github.api.LoginService;
import com.journeyOS.github.entity.BasicToken;
import com.journeyOS.github.entity.OauthToken;
import com.journeyOS.github.entity.User;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import retrofit2.Response;
import rx.Observable;

public class LoginModel extends BaseViewModel {
    static final String TAG = LoginModel.class.getSimpleName();

    MutableLiveData<StatusDataResource> mLoginStatus = new MutableLiveData<>();

    MutableLiveData<StatusDataResource> mUserStatus = new MutableLiveData<>();

    protected MutableLiveData<StatusDataResource> getLoginStatus() {
        return mLoginStatus;
    }

    protected MutableLiveData<StatusDataResource> getUserStatus() {
        return mUserStatus;
    }

    @Override
    protected void onCreate() {
    }

    @Deprecated
    protected void login(String userName, String password) {
        AuthRequestModel authRequestModel = AuthRequestModel.generate();
        String token = Credentials.basic(userName, password);
        Observable<Response<BasicToken>> observable = AppHttpClient.getInstance(token).getService(LoginService.class).authorizations(authRequestModel);
        HttpProgressSubscriber<BasicToken, Response<BasicToken>> subscriber = new HttpProgressSubscriber<>(null,
                new HttpObserver<BasicToken>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        mLoginStatus.postValue(StatusDataResource.error(error.getMessage()));
                        LogUtils.d(TAG, "login error = " + error);
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<BasicToken> response) {
                        BasicToken token = response.body();
                        if (token != null) {
                            LogUtils.d(TAG, "login success");
                            mLoginStatus.postValue(StatusDataResource.success(token));
                        } else {
                            LogUtils.d(TAG, "login success, but token was null, error message = " + response.getOriResponse().message());
                            mLoginStatus.postValue(StatusDataResource.error(response.getOriResponse().message()));
                        }

                    }
                }
        );

        HttpCoreManager.executeRxHttp(observable, subscriber);
    }

    protected String getAuthUrl() {
        long randomState = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        String authUrl = GithubConfig.AUTHURL +
                "?client_id=" + GithubConfig.CLIENT_ID +
                "&scope=" + GithubConfig.SCOPE +
                "&redirect_uri=" + GithubConfig.REDIRECT_URL +
                "&state=" + randomState;
        return authUrl;
    }

    protected void handleUrl(String url) {
        Uri uri = Uri.parse(url);
        if (uri != null) {
            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            getAccessToken(code, state);
        }
    }

    protected void getAccessToken(String code, String state) {
        LogUtils.d(TAG, "get access token, code = [" + code + "], state = [" + state + "]");
        Observable<Response<OauthToken>> observable = AppHttpClient.getInstance(GithubConfig.GITHUB_BASE_URL, null).getService(LoginService.class).getAccessToken(
                GithubConfig.CLIENT_ID,
                GithubConfig.CLIENT_SECRET,
                code, state
        );

        HttpProgressSubscriber<OauthToken, Response<OauthToken>> subscriber = new HttpProgressSubscriber<>(null,
                new HttpObserver<OauthToken>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        mLoginStatus.postValue(StatusDataResource.error(error.getMessage()));
                        LogUtils.d(TAG, "login error = " + error);
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<OauthToken> response) {
                        OauthToken token = response.body();
                        if (token != null) {
                            LogUtils.d(TAG, "login success");
                            mLoginStatus.postValue(StatusDataResource.success(BasicToken.generateFromOauthToken(token)));
                        } else {
                            LogUtils.d(TAG, "login success, but token was null, error message = " + response.getOriResponse().message());
                            mLoginStatus.postValue(StatusDataResource.error(response.getOriResponse().message()));
                        }

                    }
                }
        );

        HttpCoreManager.executeRxHttp(observable, subscriber);

    }

    protected void getUserInfo(final BasicToken basicToken) {
        HttpObserver<User> httpObserver = new HttpObserver<User>() {
            @Override
            public void onSuccess(@NonNull HttpResponse<User> response) {
                LogUtils.d(TAG, "get user info success");
                saveAuthUser(basicToken, response.body());
                mUserStatus.postValue(StatusDataResource.success(response));
            }

            @Override
            public void onError(@NonNull Throwable error) {
                LogUtils.d(TAG, "get user info error = " + error);
                mUserStatus.postValue(StatusDataResource.error(error.getMessage()));
            }

        };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<User, Response<User>>() {
            @Override
            public Observable<Response<User>> createObservable(boolean forceNetWork) {
                return AppHttpClient.getInstance(basicToken.token).getService(GithubService.class).getAuthUser(forceNetWork);
            }
        }, httpObserver, false);
    }

    void saveAuthUser(final BasicToken basicToken, final User user) {
        CoreManager.getImpl(ICoreExecutorsApi.class).diskIOThread().execute(new Runnable() {
            @Override
            public void run() {
                AuthUser authUser = new AuthUser();
                String scope = BaseUtils.list2String(basicToken.scopes);
                Date date = new Date();
                authUser.accessToken = basicToken.token;
                authUser.scope = scope;
                authUser.authTime = date.getTime();
                authUser.expireIn = 360 * 24 * 60 * 60;
                authUser.selected = true;
                authUser.loginId = user.login;
                authUser.name = user.name;
                authUser.email = user.email;
                authUser.avatar = user.avatarUrl;
                CoreManager.getImpl(IAuthUserProvider.class).insertOrUpdateAuthUser(authUser);
            }
        });

    }

}
