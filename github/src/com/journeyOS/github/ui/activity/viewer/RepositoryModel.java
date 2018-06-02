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

package com.journeyOS.github.ui.activity.viewer;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.http.AppHttpClient;
import com.journeyOS.core.http.HttpCoreManager;
import com.journeyOS.core.http.HttpObserver;
import com.journeyOS.core.http.HttpResponse;
import com.journeyOS.core.http.HttpSubscriber;
import com.journeyOS.core.viewmodel.BaseViewModel;
import com.journeyOS.github.api.GithubService;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

public class RepositoryModel extends BaseViewModel {
    static final String TAG = RepositoryModel.class.getSimpleName();

    MutableLiveData<StatusDataResource> mStarredStatus = new MutableLiveData<>();

    public MutableLiveData<StatusDataResource> getStarredStatus() {
        return mStarredStatus;
    }

    MutableLiveData<StatusDataResource> mWatchedStatus = new MutableLiveData<>();

    public MutableLiveData<StatusDataResource> getWatchedStatus() {
        return mWatchedStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAuthUser().accessToken).getService(GithubService.class);
    }

    public void starRepo(final String owner, final String repoName, final boolean star) {
        LogUtils.d(TAG, "user = "+owner + " wanna satr = "+star);
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            @Override
            public void onSuccess(@NonNull HttpResponse<ResponseBody> response) {
                LogUtils.d(TAG, "check starred success");
                checkStarred(owner, repoName);
            }

            @Override
            public void onError(@NonNull Throwable error) {
                LogUtils.d(TAG, "check starred error = " + error);
            }

        };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<ResponseBody, Response<ResponseBody>>() {
            @Override
            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                return star ? mGithubService.starRepo(owner, repoName) : mGithubService.unstarRepo(owner, repoName);
            }
        }, httpObserver);
    }

    public void checkStarred(final String owner, final String repoName) {
        HttpSubscriber<ResponseBody> httpSubscriber = new HttpSubscriber<>(
                new HttpObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(HttpResponse<ResponseBody> response) {
                        LogUtils.d(TAG, "check starred success, starred = " + response.isSuccessful());
                        mStarredStatus.postValue(StatusDataResource.success(response.isSuccessful()));
                    }

                    @Override
                    public void onError(Throwable error) {
                        LogUtils.d(TAG, "check starred error = " + error);
                        mStarredStatus.postValue(StatusDataResource.error(error.getMessage()));
                    }
                }
        );

        HttpCoreManager.executeRxHttp(mGithubService.checkRepoStarred(owner, repoName), httpSubscriber);
    }

    public void watchRepo(final String owner, final String repoName, final boolean star) {
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            @Override
            public void onSuccess(@NonNull HttpResponse<ResponseBody> response) {
                LogUtils.d(TAG, "watch repo success");
                checkWatched(owner, repoName);
            }

            @Override
            public void onError(@NonNull Throwable error) {
                LogUtils.d(TAG, "watch repo error = " + error);
            }

        };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<ResponseBody, Response<ResponseBody>>() {
            @Override
            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                return star ? mGithubService.watchRepo(owner, repoName) : mGithubService.unwatchRepo(owner, repoName);
            }
        }, httpObserver, false);
    }

    public void checkWatched(final String owner, final String repoName) {
        HttpSubscriber<ResponseBody> httpSubscriber = new HttpSubscriber<>(
                new HttpObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(HttpResponse<ResponseBody> response) {
                        LogUtils.d(TAG, "check watched success, starred = " + response.isSuccessful());
                        mWatchedStatus.postValue(StatusDataResource.success(response.isSuccessful()));
                    }

                    @Override
                    public void onError(Throwable error) {
                        LogUtils.d(TAG, "check watched error = " + error);
                        mWatchedStatus.postValue(StatusDataResource.error(error.getMessage()));
                    }
                }
        );

        HttpCoreManager.executeRxHttp(mGithubService.checkRepoWatched(owner, repoName), httpSubscriber);
    }
}
