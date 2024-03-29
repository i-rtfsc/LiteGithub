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

import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.journeyOS.github.entity.SearchResult;
import com.journeyOS.github.entity.filter.ReposFilter;
import com.journeyOS.github.type.RepoType;
import com.journeyOS.github.ui.activity.search.SearchFilter;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryData;

import java.util.ArrayList;

import retrofit2.Response;
import rx.Observable;

public class ReposModel extends BaseViewModel {
    static final boolean DEBUG = BuildConfig.DEBUG;
    static final String TAG = ReposModel.class.getSimpleName();

    MutableLiveData<StatusDataResource> mReposStatus = new MutableLiveData<>();

    public MutableLiveData<StatusDataResource> getReposStatus() {
        return mReposStatus;
    }

    MutableLiveData<StatusDataResource> mSearchReposStatus = new MutableLiveData<>();

    protected MutableLiveData<StatusDataResource> getSearchReposStatus() {
        return mSearchReposStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAuthUser().accessToken).getService(GithubService.class);
    }

    public void loadRepositories(final RepoType repoType, @NonNull final String user, final int page) {
        LogUtils.d(TAG, "loadRepositories(ReposFragment) called with: repoType = [" + repoType + "], user = [" + user + "], page = [" + page + "]");
        HttpObserver<ArrayList<Repository>> httpObserver = new HttpObserver<ArrayList<Repository>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<Repository>> response) {
                LogUtils.d(TAG, "load repository " + repoType + " success");
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
                switch (repoType) {
                    case OWNED:
                        responseObservable = mGithubService.getUserRepos(forceNetWork, page,
                                ReposFilter.DEFAULT.type, ReposFilter.DEFAULT.sort, ReposFilter.DEFAULT.direction);
                        break;
                    case PUBLIC:
                        responseObservable = mGithubService.getUserPublicRepos(forceNetWork, user, page,
                                ReposFilter.DEFAULT.type, ReposFilter.DEFAULT.sort, ReposFilter.DEFAULT.direction);
                        break;
                    case STARRED:
                        responseObservable = mGithubService.getUserStarred(forceNetWork, user, page,
                                ReposFilter.DEFAULT.sort, ReposFilter.DEFAULT.direction);
                        break;
                    default:
                        responseObservable = mGithubService.getUserRepos(forceNetWork, page,
                                ReposFilter.DEFAULT.type, ReposFilter.DEFAULT.sort, ReposFilter.DEFAULT.direction);
                        break;
                }
                return responseObservable;
            }
        }, httpObserver, false);
    }

    protected void searchRepositories(final SearchFilter searchFilter, final int page) {
        HttpObserver<SearchResult<Repository>> httpObserver = new HttpObserver<SearchResult<Repository>>() {
            @Override
            public void onSuccess(HttpResponse<SearchResult<Repository>> response) {
                LogUtils.d(TAG, "load repository success");
                ArrayList<RepositoryData> repositoryDataArrayList = convertFromRepository((ArrayList<Repository>) response.body().items);
                mSearchReposStatus.postValue(StatusDataResource.success(repositoryDataArrayList, page));
            }

            @Override
            public void onError(Throwable error) {
                LogUtils.d(TAG, "load repository error = " + error.getMessage());
                mSearchReposStatus.postValue(StatusDataResource.error(error.getMessage()));
            }
        };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<SearchResult<Repository>,
                Response<SearchResult<Repository>>>() {
            @Nullable
            @Override
            public Observable<Response<SearchResult<Repository>>> createObservable(boolean forceNetWork) {
                return mGithubService.searchRepos(searchFilter.getQuery(), searchFilter.getSort(), searchFilter.getOrder(), page);
            }
        }, httpObserver);
    }

    ArrayList<RepositoryData> convertFromRepository(ArrayList<Repository> repositories) {
        ArrayList<RepositoryData> repositoryDataArrayList = new ArrayList<>();
        for (Repository repository : repositories) {
            RepositoryData repositoryData = new RepositoryData();
            repositoryData.name = repository.name;
            repositoryData.defaultBranch = repository.defaultBranch;
            repositoryData.description = repository.description;
            repositoryData.size = repository.size;
            repositoryData.stargazersCount = repository.stargazersCount;
            repositoryData.forksCount = repository.forksCount;
            repositoryData.owner = repository.owner;
            repositoryData.language = repository.language;
            repositoryData.openIssuesCount = repository.openIssuesCount;
            repositoryData.hasIssues = repository.hasIssues;
            repositoryData.subscribersCount = repository.subscribersCount;
            repositoryData.fork = repository.fork;
            repositoryData.createdAt = repository.createdAt;
            repositoryData.pushedAt = repository.pushedAt;
            repositoryData.updatedAt = repository.updatedAt;
            repositoryData.fullName = repository.fullName;
            repositoryData.htmlUrl = repository.htmlUrl;
            repositoryDataArrayList.add(repositoryData);
            if (DEBUG)
                LogUtils.d(TAG, "convert from repository, source data = " + repositoryData.toString());
            if (DEBUG)
                LogUtils.d(TAG, "convert from repository, target data = " + repository.toString());
        }

        return repositoryDataArrayList;
    }
}
