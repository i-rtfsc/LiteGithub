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

package com.journeyOS.github.ui.activity.profile;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.http.AppHttpClient;
import com.journeyOS.core.http.HttpCoreManager;
import com.journeyOS.core.http.HttpObserver;
import com.journeyOS.core.http.HttpResponse;
import com.journeyOS.core.viewmodel.BaseViewModel;
import com.journeyOS.github.api.GithubService;
import com.journeyOS.github.entity.User;

import retrofit2.Response;
import rx.Observable;

public class ProfileModel extends BaseViewModel {
    static final String TAG = ProfileModel.class.getSimpleName();

    MutableLiveData<StatusDataResource> mUserStatus = new MutableLiveData<>();

    protected MutableLiveData<StatusDataResource> getUserStatus() {
        return mUserStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAccessToken()).getService(GithubService.class);
    }

    protected void getUserInfo(final String login) {
        HttpObserver<User> httpObserver = new HttpObserver<User>() {
            @Override
            public void onSuccess(@NonNull HttpResponse<User> response) {
                LogUtils.d(TAG, "get user info success");
                mUserStatus.postValue(StatusDataResource.success(response.body()));
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
                return mGithubService.getUser(true, login);
            }
        }, httpObserver, false);
    }
}
