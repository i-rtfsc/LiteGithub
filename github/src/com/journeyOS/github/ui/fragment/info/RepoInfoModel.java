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

package com.journeyOS.github.ui.fragment.info;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.http.AppHttpClient;
import com.journeyOS.core.http.HttpCoreManager;
import com.journeyOS.core.http.HttpObserver;
import com.journeyOS.core.http.HttpResponse;
import com.journeyOS.core.viewmodel.BaseViewModel;
import com.journeyOS.github.api.GithubService;
import com.journeyOS.github.ui.fragment.files.ReposFileModel;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

public class RepoInfoModel extends BaseViewModel {

    static final String TAG = ReposFileModel.class.getSimpleName();

    MutableLiveData<StatusDataResource> mReposInfoStatus = new MutableLiveData<>();

    protected MutableLiveData<StatusDataResource> getReposInfoStatus() {
        return mReposInfoStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAuthUser().accessToken).getService(GithubService.class);
    }

    protected void loadReadMe(final String readmeFileUrl) {
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            @Override
            public void onSuccess(HttpResponse<ResponseBody> response) {
                LogUtils.d(TAG, "load repos files success");

                mReposInfoStatus.postValue(StatusDataResource.success(response.body()));
            }

            @Override
            public void onError(Throwable error) {
                LogUtils.d(TAG, "load repos files error = " + error.getMessage());
                mReposInfoStatus.postValue(StatusDataResource.error(error.getMessage()));
            }
        };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<ResponseBody,
                Response<ResponseBody>>() {
            @Nullable
            @Override
            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                return mGithubService.getFileAsHtmlStream(forceNetWork, readmeFileUrl);
            }
        }, httpObserver, false);
    }
}
