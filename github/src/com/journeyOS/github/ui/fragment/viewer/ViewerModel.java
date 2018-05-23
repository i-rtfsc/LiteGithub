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

package com.journeyOS.github.ui.fragment.viewer;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.MarkdownHelper;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.http.AppHttpClient;
import com.journeyOS.core.http.HttpCoreManager;
import com.journeyOS.core.http.HttpObserver;
import com.journeyOS.core.http.HttpResponse;
import com.journeyOS.core.viewmodel.BaseViewModel;
import com.journeyOS.github.api.GithubService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

public class ViewerModel extends BaseViewModel {
    private static final String TAG = ViewerModel.class.getSimpleName();

    String downloadSource;

    MutableLiveData<StatusDataResource> mCodeStatus = new MutableLiveData<>();

    protected MutableLiveData<StatusDataResource> getCodeStatus() {
        return mCodeStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAccessToken()).getService(GithubService.class);
    }

    protected void load(@NonNull final String url, @NonNull final String htmlUrl, @NonNull final String downloadUrl, boolean isReload) {
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            @Override
            public void onSuccess(HttpResponse<ResponseBody> response) {
                LogUtils.d(TAG, "load repos files success");
                try {
                    downloadSource = response.body().string();
                    mCodeStatus.postValue(StatusDataResource.success(downloadSource));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable error) {
                LogUtils.d(TAG, "load repos files error = " + error.getMessage());
                mCodeStatus.postValue(StatusDataResource.error(error.getMessage()));
            }
        };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<ResponseBody,
                Response<ResponseBody>>() {
            @Nullable
            @Override
            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                return MarkdownHelper.isMarkdown(url) ?
                        mGithubService.getFileAsHtmlStream(forceNetWork, url) :
                        mGithubService.getFileAsStream(forceNetWork, url);
            }
        }, httpObserver, false);
    }

}
