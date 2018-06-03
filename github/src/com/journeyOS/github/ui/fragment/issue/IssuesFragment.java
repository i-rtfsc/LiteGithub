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

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.base.BaseListFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.IssuesFilter;
import com.journeyOS.github.type.IssueType;
import com.journeyOS.github.ui.fragment.issue.adapter.IssuesData;
import com.journeyOS.github.ui.fragment.issue.adapter.IssuesHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssuesFragment extends BaseListFragment {
    static final String TAG = IssuesFragment.class.getSimpleName();

    static final String EXTRA_ISSUES_FILTER = "issuesFilter";
    static final String EXTRA_USER = "user";
    static final String EXTRA_REPO = "repo";

    IssuesFilter mIssuesFilter;
    String mUserId;
    String mRepoName;

    IssuesModel mIssuesModel;

    Map<Integer, Object> initedMap = new HashMap<>();

    final Observer<StatusDataResource> issuesStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleIssuesStatusObserver(statusDataResource);
        }
    };

    public static Fragment newInstanceForUser(@NonNull IssuesFilter issuesFilter) {
        IssuesFragment fragment = new IssuesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ISSUES_FILTER, issuesFilter);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstanceForRepo(@NonNull IssuesFilter issuesFilter, @NonNull String userId, @NonNull String repoName) {
        IssuesFragment fragment = new IssuesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ISSUES_FILTER, issuesFilter);
        bundle.putString(EXTRA_USER, userId);
        bundle.putString(EXTRA_REPO, repoName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mIssuesFilter = getArguments().getParcelable(EXTRA_ISSUES_FILTER);
        if (mIssuesFilter.issueType == IssueType.REPO) {
            mUserId = getArguments().getString(EXTRA_USER);
            mRepoName = getArguments().getString(EXTRA_REPO);
        }
        initedMap.put(1, false);
    }

    @Override
    public void initViews() {
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mIssuesModel = ModelProvider.getModel(this, IssuesModel.class);

        showLoading();
        if (mIssuesFilter.issueType == IssueType.REPO) {
            mIssuesModel.loadRepoIssues(mIssuesFilter, mUserId, mRepoName, getCurPage(), false);
            mIssuesModel.getRepoIssuesStatus().observe(this, issuesStatusObserver);
        } else if (mIssuesFilter.issueType == IssueType.USER) {
            mIssuesModel.loadUserIssues(mIssuesFilter, getCurPage(), false);
            mIssuesModel.getUserIssuesStatus().observe(this, issuesStatusObserver);
        }
    }

    @Override
    public Class<? extends BaseViewHolder> attachViewHolder() {
        return IssuesHolder.class;
    }

    @Override
    public int attachViewHolderRes() {
        return R.layout.layout_item_issue;
    }

    @Override
    public void onLoadMore(int page) {
        Object initedByPage = initedMap.get(page);
        if (BaseUtils.isNull(initedByPage)) {
            LogUtils.d(TAG, "current page = " + page + ", has been inited = " + initedByPage);
            showLoading();
            initedMap.put(page, false);

            if (mIssuesFilter.issueType == IssueType.REPO) {
                mIssuesModel.loadRepoIssues(mIssuesFilter, mUserId, mRepoName, page, false);
            } else if (mIssuesFilter.issueType == IssueType.USER) {
                mIssuesModel.loadUserIssues(mIssuesFilter, page, false);
            }
        }
    }

    @Override
    public void onReLoadData() {
        initedMap.clear();
        initedMap.put(1, false);
        mAdapter.clear();
        showLoading();
        onLoadMore(1);
    }

    void handleIssuesStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                hideLoading();
                int page = (Integer) statusDataResource.subData;
                Object initedByPage = initedMap.get(page);
                LogUtils.d(TAG, "(onChanged)current page = " + page + ", has been inited = " + initedByPage);
                if (!BaseUtils.isNull(initedByPage) && (!(Boolean) initedByPage)) {
                    initedMap.put(page, true);
                    if (page == 1) {
                        initIssues((List<IssuesData>) statusDataResource.data);
                    } else {
                        updateIssues((List<IssuesData>) statusDataResource.data);
                    }
                }
                break;
            case ERROR:
                hideLoading();
                showTipDialog(statusDataResource.message);
                break;
        }
    }

    void initIssues(List<IssuesData> issuesData) {
        LogUtils.d(TAG, "init adapter data");
        mAdapter.setData(issuesData);
    }

    void updateIssues(List<IssuesData> issuesData) {
        LogUtils.d(TAG, "defore upate adapter data = " + mAdapter.getItemCount());
        mAdapter.addData(issuesData);
        LogUtils.d(TAG, "after upate adapter data = " + mAdapter.getItemCount());
    }
}
