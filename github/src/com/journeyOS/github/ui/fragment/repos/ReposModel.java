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

package com.journeyOS.github.ui.fragment.repos;

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
import com.journeyOS.github.BuildConfig;
import com.journeyOS.github.api.GithubService;
import com.journeyOS.github.entity.Repository;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryData;

import java.util.ArrayList;

import retrofit2.Response;
import rx.Observable;

public class ReposModel extends BaseViewModel {
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = ReposModel.class.getSimpleName();

    private MutableLiveData<StatusDataResource> mReposStatus = new MutableLiveData<>();

    protected MutableLiveData<StatusDataResource> getReposStatus() {
        return mReposStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAccessToken()).getService(GithubService.class);
    }

    protected void loadRepositories(final ReposFragment.ReposType reposType, final int page) {
        HttpObserver<ArrayList<Repository>> httpObserver = new HttpObserver<ArrayList<Repository>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<Repository>> response) {
                LogUtils.d(TAG, "load repository " + reposType + " success");
                ArrayList<RepositoryData> repositoryDataArrayList = convertFromRepository((ArrayList<Repository>) response.body());
                mReposStatus.postValue(StatusDataResource.success(repositoryDataArrayList, page));
            }

            @Override
            public void onError(Throwable error) {
                LogUtils.d(TAG, "load repository error = " + error.getMessage());
                mReposStatus.postValue(StatusDataResource.error(error.getMessage()));
            }
        };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<ArrayList<Repository>,
                Response<ArrayList<Repository>>>() {
            @Nullable
            @Override
            public Observable<Response<ArrayList<Repository>>> createObservable(boolean forceNetWork) {
                Observable<Response<ArrayList<Repository>>> responseObservable = null;
                switch (reposType) {
                    case OWNED:
                        responseObservable = mGithubService.getUserRepos(forceNetWork, "", page);
                        break;
                    case STARRED:
                        responseObservable = mGithubService.getUserStarred(forceNetWork, "", page);
                        break;
                    default:
                        responseObservable = mGithubService.getUserRepos(forceNetWork, "", page);
                        break;
                }
                return responseObservable;
            }
        }, httpObserver, true);
    }

    ArrayList<RepositoryData> convertFromRepository(ArrayList<Repository> repositories) {
        ArrayList<RepositoryData> repositoryDataArrayList = new ArrayList<>();
        for (Repository repository : repositories) {
            RepositoryData repositoryData = new RepositoryData();
            repositoryData.name = repository.name;
            repositoryData.defaultBranch = repository.defaultBranch;
            repositoryData.description = repository.description;
            repositoryData.stargazersCount = repository.stargazersCount;
            repositoryData.forksCount = repository.forksCount;
            repositoryData.owner = repository.owner;
            repositoryData.language = repository.language;
            repositoryDataArrayList.add(repositoryData);
            if (DEBUG)
                LogUtils.d(TAG, "convert from repository, source data = " + repositoryData.toString());
            if (DEBUG)
                LogUtils.d(TAG, "convert from repository, target data = " + repository.toString());
        }

        return repositoryDataArrayList;
    }
}
