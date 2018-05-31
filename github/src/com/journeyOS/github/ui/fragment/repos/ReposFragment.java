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

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.Messages;
import com.journeyOS.core.base.BaseListFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.activity.search.SearchFilter;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryData;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryHolder;
import com.journeyOS.literouter.RouterListener;
import com.journeyOS.literouter.RouterMsssage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReposFragment extends BaseListFragment implements RouterListener {
    static final String TAG = ReposFragment.class.getSimpleName();

    Context mContext;
    ReposModel mReposModel;

    Map<Integer, Object> initedMap = new HashMap<>();

    final Observer<StatusDataResource> reposStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleReposStatusObserver(statusDataResource);
        }
    };

    public enum ReposType {
        OWNED, STARRED
    }

    static final String EXTRA_REPOS_TYPE = "reposType";
    ReposType mReposType = null;

    public static Fragment newInstance(ReposType reposType) {
        ReposFragment fragment = new ReposFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_REPOS_TYPE, reposType);
        fragment.setArguments(bundle);
        return fragment;
    }

    static final String EXTRA_SEARCH_FILTER = "searchFilter";
    SearchFilter mSearchFilter = null;

    public static Fragment newInstanceForSearch(SearchFilter searchFilter) {
        ReposFragment fragment = new ReposFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SEARCH_FILTER, searchFilter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
        mReposType = (ReposType) getArguments().get(EXTRA_REPOS_TYPE);
        mSearchFilter = getArguments().getParcelable(EXTRA_SEARCH_FILTER);
        initedMap.put(1, false);
    }

    @Override
    public void initViews() {

    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mReposModel = ModelProvider.getModel(this, ReposModel.class);

        showLoading();
        if (!BaseUtils.isNull(mReposType)) {
            mReposModel.loadRepositories(mReposType, getCurPage());
            mReposModel.getReposStatus().observe(this, reposStatusObserver);
        }

        if (!BaseUtils.isNull(mSearchFilter)) {
            mReposModel.searchRepositories(mSearchFilter, getCurPage());
            mReposModel.getSearchReposStatus().observe(this, reposStatusObserver);
        }
    }

    @Override
    public Class<? extends BaseViewHolder> attachViewHolder() {
        return RepositoryHolder.class;
    }

    @Override
    public int attachViewHolderRes() {
        return R.layout.layout_item_repository;
    }

    @Override
    public void onLoadMore(int page) {
        Object initedByPage = initedMap.get(page);
        if (BaseUtils.isNull(initedByPage)) {
            LogUtils.d(TAG, "current page = " + page + ", has been inited = " + initedByPage);
            showLoading();
            initedMap.put(page, false);

            if (!BaseUtils.isNull(mReposType)) {
                mReposModel.loadRepositories(mReposType, page);
            }

            if (!BaseUtils.isNull(mSearchFilter)) {
                mReposModel.searchRepositories(mSearchFilter, getCurPage());
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

    void handleReposStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                hideLoading();
                int page = (Integer) statusDataResource.subData;
                Object initedByPage = initedMap.get(page);
                LogUtils.d(TAG, "(onChanged)current page = " + page + ", has been inited = " + initedByPage);
                if (!BaseUtils.isNull(initedByPage) && (!(Boolean) initedByPage)) {
                    initedMap.put(page, true);
                    if (page == 1) {
                        initRepositories((List<RepositoryData>) statusDataResource.data);
                    } else {
                        updateRepositories((List<RepositoryData>) statusDataResource.data);
                    }
                }
                break;
            case ERROR:
                hideLoading();
                showTipDialog(statusDataResource.message);
                break;
        }
    }

    void initRepositories(List<RepositoryData> repositories) {
        LogUtils.d(TAG, "init adapter data");
        mAdapter.setData(repositories);
    }

    void updateRepositories(List<RepositoryData> repositories) {
        LogUtils.d(TAG, "defore upate adapter data = " + mAdapter.getItemCount());
        mAdapter.addData(repositories);
        LogUtils.d(TAG, "after upate adapter data = " + mAdapter.getItemCount());
    }

    @Override
    public void onShowMessage(RouterMsssage message) {
        Messages msg = (Messages) message;
        LogUtils.d(LogUtils.TAG, "postSearchEvent = "+msg.what + " , SearchFilter = "+msg.obj);
        switch (msg.what) {
            case Messages.MSG_SEARCHING:
                initedMap.clear();
                initedMap.put(1, false);
                mAdapter.clear();
                showLoading();
                mSearchFilter = (SearchFilter) msg.obj;
                mReposModel.searchRepositories(mSearchFilter, 1);
                break;
        }
    }
}
