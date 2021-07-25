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

import androidx.lifecycle.Observer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.base.BaseListFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.issue.adapter.IssuesData;
import com.journeyOS.github.ui.fragment.issue.detail.adapter.IssueDetailsData;
import com.journeyOS.github.ui.fragment.issue.detail.adapter.IssueDetailsHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueDetailsFragment extends BaseListFragment {
    static final String TAG = IssueDetailsFragment.class.getSimpleName();
    IssueDetailsModel mIssueDetailsModel;
    Map<Integer, Object> initedMap = new HashMap<>();

    static final String EXTRA_ISSUES_DATA = "issuesData";
    IssuesData mIssuesData;

    final Observer<StatusDataResource> commentsObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleComments(statusDataResource);
        }
    };

    public static Fragment newInstance(@NonNull IssuesData issuesData) {
        IssueDetailsFragment fragment = new IssueDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ISSUES_DATA, issuesData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        initedMap.put(1, false);
    }

    @Override
    public void initViews() {

    }

    @Override
    public Class<? extends BaseViewHolder> attachViewHolder() {
        return IssueDetailsHolder.class;
    }

    @Override
    public int attachViewHolderRes() {
        return R.layout.layout_item_comments;
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mIssuesData = getArguments().getParcelable(EXTRA_ISSUES_DATA);

        mIssueDetailsModel = ModelProvider.getModel(this, IssueDetailsModel.class);
        mIssueDetailsModel.loadComments(mIssuesData.repoAuthorName, mIssuesData.repoName, mIssuesData.number, 1, true);

        mIssueDetailsModel.getIssuesStatus().observe(this, commentsObserver);
    }

    @Override
    public void onLoadMore(int page) {
        Object initedByPage = initedMap.get(page);
        if (BaseUtils.isNull(initedByPage)) {
            LogUtils.d(TAG, "current page = " + page + ", has been inited = " + initedByPage);
            showLoading();
            initedMap.put(page, false);

            mIssueDetailsModel.loadComments(mIssuesData.repoAuthorName, mIssuesData.repoName, mIssuesData.number, page, true);
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

    void handleComments(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                hideLoading();
                int page = (Integer) statusDataResource.subData;
                Object initedByPage = initedMap.get(page);
                LogUtils.d(TAG, "(onChanged)current page = " + page + ", has been inited = " + initedByPage);
                if (!BaseUtils.isNull(initedByPage) && (!(Boolean) initedByPage)) {
                    initedMap.put(page, true);
                    if (page == 1) {
                        initComments((List<IssueDetailsData>) statusDataResource.data);
                    } else {
                        updateComments((List<IssueDetailsData>) statusDataResource.data);
                    }
                }
                break;
            case ERROR:
                hideLoading();
                showTipDialog(statusDataResource.message);
                break;
        }
    }

    void updateComments(List<IssueDetailsData> data) {
        LogUtils.d(TAG, "init adapter data");
        mAdapter.setData(data);
    }

    void initComments(List<IssueDetailsData> data) {
        LogUtils.d(TAG, "defore upate adapter data = " + mAdapter.getItemCount());
        mAdapter.addData(data);
        LogUtils.d(TAG, "after upate adapter data = " + mAdapter.getItemCount());
    }
}
