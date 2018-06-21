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

package com.journeyOS.github.ui.fragment.issue.detail;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import com.journeyOS.base.utils.BaseUtils;
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
import com.journeyOS.github.entity.IssueEvent;
import com.journeyOS.github.ui.fragment.issue.detail.adapter.IssueDetailsData;

import java.util.ArrayList;

import retrofit2.Response;
import rx.Observable;

public class IssueDetailsModel extends BaseViewModel {
    static final boolean DEBUG = BuildConfig.DEBUG;
    static final String TAG = IssueDetailsModel.class.getSimpleName();

    MutableLiveData<StatusDataResource> mIssuesStatus = new MutableLiveData<>();

    public MutableLiveData<StatusDataResource> getIssuesStatus() {
        return mIssuesStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAuthUser().accessToken).getService(GithubService.class);
    }

    public void loadComments(final String owner, final String repoName, final int issueNumber, final int page, final boolean isReload) {
        final boolean readCacheFirst = page == 1 && !isReload;

        HttpObserver<ArrayList<IssueEvent>> httpObserver =
                new HttpObserver<ArrayList<IssueEvent>>() {
                    @Override
                    public void onSuccess(HttpResponse<ArrayList<IssueEvent>> response) {
                        LogUtils.d(TAG, "load user issues success");
                        ArrayList<IssueDetailsData> issuesData = convertFromIssueEvent((ArrayList<IssueEvent>) response.body());
                        mIssuesStatus.postValue(StatusDataResource.success(issuesData, page));
                    }

                    @Override
                    public void onError(Throwable error) {
                        LogUtils.d(TAG, "load user issues error = " + error.getMessage());
                        mIssuesStatus.postValue(StatusDataResource.error(error.getMessage()));
                    }
                };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<ArrayList<IssueEvent>,
                Response<ArrayList<IssueEvent>>>() {
            @Nullable
            @Override
            public Observable<Response<ArrayList<IssueEvent>>> createObservable(boolean forceNetWork) {
                return mGithubService.getIssueTimeline(forceNetWork, owner,
                        repoName, issueNumber, page);
            }
        }, httpObserver, readCacheFirst);
    }


    ArrayList<IssueDetailsData> convertFromIssueEvent(ArrayList<IssueEvent> issueEvents) {
        ArrayList<IssueDetailsData> issueDetailsDataArrayList = new ArrayList<>();
        for (IssueEvent issueEvent : issueEvents) {
            if (BaseUtils.isNull(issueEvent.type)) {
                continue;
            }
            IssueDetailsData detailsData = new IssueDetailsData();
            detailsData.type = issueEvent.type;
            detailsData.user = issueEvent.user;
            detailsData.assignee = issueEvent.assignee;
            detailsData.actor = issueEvent.actor;
            detailsData.createdAt = issueEvent.createdAt;
            detailsData.parentIssue = issueEvent.parentIssue;
            detailsData.body = issueEvent.body;
            detailsData.bodyHtml = issueEvent.bodyHtml;
            detailsData.source = issueEvent.source;
            detailsData.milestone = issueEvent.milestone;
            detailsData.label = issueEvent.label;
            issueDetailsDataArrayList.add(detailsData);
            if (!DEBUG)
                LogUtils.d(TAG, "convert from issue event, source data = " + issueEvent.toString());
            if (DEBUG)
                LogUtils.d(TAG, "convert from issue event, target data = " + detailsData.toString());
        }

        return issueDetailsDataArrayList;
    }

}