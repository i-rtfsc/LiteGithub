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

package com.journeyOS.github.ui.fragment.issue;

import androidx.lifecycle.MutableLiveData;
import androidx.annotation.Nullable;

import com.journeyOS.base.utils.IssueUtils;
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
import com.journeyOS.github.entity.Issue;
import com.journeyOS.github.entity.filter.IssuesFilter;
import com.journeyOS.github.entity.SearchResult;
import com.journeyOS.github.ui.fragment.issue.adapter.IssuesData;

import java.util.ArrayList;

import retrofit2.Response;
import rx.Observable;

public class IssuesModel extends BaseViewModel {
    static final boolean DEBUG = BuildConfig.DEBUG;
    static final String TAG = IssuesModel.class.getSimpleName();

    MutableLiveData<StatusDataResource> mUserIssuesStatus = new MutableLiveData<>();

    public MutableLiveData<StatusDataResource> getUserIssuesStatus() {
        return mUserIssuesStatus;
    }

    MutableLiveData<StatusDataResource> mRepoIssuesStatus = new MutableLiveData<>();

    public MutableLiveData<StatusDataResource> getRepoIssuesStatus() {
        return mRepoIssuesStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAuthUser().accessToken).getService(GithubService.class);
    }

    public void loadUserIssues(final IssuesFilter issuesFilter, final int page, final boolean isReload) {
        final boolean readCacheFirst = page == 1 && !isReload;

        HttpObserver<SearchResult<Issue>> httpObserver =
                new HttpObserver<SearchResult<Issue>>() {
                    @Override
                    public void onSuccess(HttpResponse<SearchResult<Issue>> response) {
                        LogUtils.d(TAG, "load user issues success");
                        ArrayList<IssuesData> issuesData = convertFromIssue((ArrayList<Issue>) response.body().items, true);
                        mUserIssuesStatus.postValue(StatusDataResource.success(issuesData, page));
                    }

                    @Override
                    public void onError(Throwable error) {
                        LogUtils.d(TAG, "load user issues error = " + error.getMessage());
                        mUserIssuesStatus.postValue(StatusDataResource.error(error.getMessage()));
                    }
                };


        final String sortStr = issuesFilter.issuesSortType.name().toLowerCase();
        final String sortDirectionStr = issuesFilter.sortDirection.name().toLowerCase();
        final String queryStr = getUserQueryStr(issuesFilter);

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<SearchResult<Issue>,
                Response<SearchResult<Issue>>>() {
            @Nullable
            @Override
            public Observable<Response<SearchResult<Issue>>> createObservable(boolean forceNetWork) {
                return mGithubService.searchIssues(forceNetWork, queryStr, sortStr, sortDirectionStr, page);
            }
        }, httpObserver, readCacheFirst);
    }


    public void loadRepoIssues(final IssuesFilter issuesFilter, final String userId, final String repoName, final int page, final boolean isReload) {
        final boolean readCacheFirst = page == 1 && !isReload;

        HttpObserver<ArrayList<Issue>> httpObserver =
                new HttpObserver<ArrayList<Issue>>() {
                    @Override
                    public void onSuccess(HttpResponse<ArrayList<Issue>> response) {
                        LogUtils.d(TAG, "load repo issues success");
                        ArrayList<IssuesData> issuesData = convertFromIssue((ArrayList<Issue>) response.body(), false);
                        mRepoIssuesStatus.postValue(StatusDataResource.success(issuesData, page));
                    }

                    @Override
                    public void onError(Throwable error) {
                        LogUtils.d(TAG, "load repo issues error = " + error.getMessage());
                        mRepoIssuesStatus.postValue(StatusDataResource.error(error.getMessage()));
                    }
                };


        final String statusStr = issuesFilter.issueState.name().toLowerCase();
        final String sortStr = issuesFilter.issuesSortType.name().toLowerCase();
        final String sortDirectionStr = issuesFilter.sortDirection.name().toLowerCase();

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<ArrayList<Issue>,
                Response<ArrayList<Issue>>>() {
            @Nullable
            @Override
            public Observable<Response<ArrayList<Issue>>> createObservable(boolean forceNetWork) {
                return mGithubService.getRepoIssues(forceNetWork, userId, repoName, statusStr, sortStr, sortDirectionStr, page);
            }
        }, httpObserver, readCacheFirst);
    }

    String getUserQueryStr(IssuesFilter issuesFilter) {
        String queryStr = "";
        String action = "";
        switch (issuesFilter.userIssuesFilterType) {
            case ALL:
                action = "involves";
                break;
            case CREATED:
                action = "author";
                break;
            case ASSIGNED:
                action = "assignee";
                break;
            case MENTIONED:
                action = "mentions";
                break;
        }
        queryStr = queryStr + action + ":" + CoreManager.getAuthUser().loginId
                + "+" + "state:" + issuesFilter.issueState.name().toLowerCase();
        return queryStr;
    }

    ArrayList<IssuesData> convertFromIssue(ArrayList<Issue> issues, boolean isUserIssues) {
        ArrayList<IssuesData> issuesDataArrayList = new ArrayList<>();
        for (Issue issue : issues) {
            IssuesData issuesData = new IssuesData();
            issuesData.number = issue.number;
            issuesData.title = issue.title;
            issuesData.commentNum = issue.commentNum;
            issuesData.user = issue.user;
            issuesData.createdAt = issue.createdAt;
            issuesData.state = issue.state;
            issuesData.isUserIssues = isUserIssues;
            issuesData.repoFullName = IssueUtils.getRepoFullName(issue.repoUrl);
            issuesData.repoName = IssueUtils.getRepoName(issue.repoUrl);;
            issuesData.repoAuthorName = IssueUtils.getRepoAuthorName(issue.repoUrl);;
            issuesDataArrayList.add(issuesData);
            if (DEBUG)
                LogUtils.d(TAG, "convert from issue, source data = " + issue.toString());
            if (DEBUG)
                LogUtils.d(TAG, "convert from issue, target data = " + issuesData.toString());
        }

        return issuesDataArrayList;
    }
}
