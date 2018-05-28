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

package com.journeyOS.github.ui.fragment.files;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.http.AppHttpClient;
import com.journeyOS.core.http.HttpCoreManager;
import com.journeyOS.core.http.HttpObserver;
import com.journeyOS.core.http.HttpResponse;
import com.journeyOS.core.viewmodel.BaseViewModel;
import com.journeyOS.github.BuildConfig;
import com.journeyOS.github.api.GithubService;
import com.journeyOS.github.entity.FileModel;
import com.journeyOS.github.ui.fragment.files.adapter.ReposFileData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Response;
import rx.Observable;

public class ReposFileModel extends BaseViewModel {
    static final boolean DEBUG = BuildConfig.DEBUG;
    static final String TAG = ReposFileModel.class.getSimpleName();

    MutableLiveData<StatusDataResource> mReposFilesStatus = new MutableLiveData<>();

    protected MutableLiveData<StatusDataResource> getReposFilesStatus() {
        return mReposFilesStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAccessToken()).getService(GithubService.class);
    }

    protected void loadFiles(@NonNull final String login, @NonNull final String name, @NonNull final String branch, @NonNull final String path, boolean isReload) {

        HttpObserver<ArrayList<FileModel>> httpObserver = new HttpObserver<ArrayList<FileModel>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<FileModel>> response) {
                LogUtils.d(TAG, "load repos files success");
                ArrayList<ReposFileData> reposFileData = convertFromFileModel((ArrayList<FileModel>) response.body());
                mReposFilesStatus.postValue(StatusDataResource.success(reposFileData));
            }

            @Override
            public void onError(Throwable error) {
                LogUtils.d(TAG, "load repos files error = " + error.getMessage());
                mReposFilesStatus.postValue(StatusDataResource.error(error.getMessage()));
            }
        };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<ArrayList<FileModel>,
                Response<ArrayList<FileModel>>>() {
            @Nullable
            @Override
            public Observable<Response<ArrayList<FileModel>>> createObservable(boolean forceNetWork) {
                return mGithubService.getRepoFiles(login, name, path, branch);
            }
        }, httpObserver, false);
    }

    ArrayList<ReposFileData> convertFromFileModel(ArrayList<FileModel> fileModels) {
        ArrayList<ReposFileData> reposFileDataArrayList = new ArrayList<>();
        for (FileModel fileModel : fileModels) {
            ReposFileData reposFileData = new ReposFileData();
            reposFileData.name = fileModel.name;
            reposFileData.size = fileModel.size;
            reposFileData.path = fileModel.path;
            reposFileData.isFile = fileModel.type.equals("file");
            reposFileData.isDir = fileModel.type.equals("dir");
            reposFileData.url = fileModel.url;
            reposFileData.htmlUrl = fileModel.url;
            reposFileData.downloadUrl = fileModel.downloadUrl;
            reposFileDataArrayList.add(reposFileData);
            if (DEBUG)
                LogUtils.d(TAG, "convert from fileModel, source data = " + fileModel.toString());
            if (DEBUG)
                LogUtils.d(TAG, "convert from fileModel, target data = " + reposFileData.toString());
        }
        Collections.sort(reposFileDataArrayList, new OrderAsc());
        return reposFileDataArrayList;
    }


    static class OrderAsc implements Comparator<ReposFileData> {
        @Override
        public int compare(ReposFileData a, ReposFileData b) {
            return Boolean.compare(a.isFile, b.isFile);
        }
    }
}
