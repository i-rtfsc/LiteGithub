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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.ViewUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.BuildConfig;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryData;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ReposFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = ReposFragment.class.getSimpleName();
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.repos_recyclerView)
    RecyclerView mReposRecyclerView;

    private BaseRecyclerAdapter mReposAdapter;

    private Context mContext;
    private ReposModel mReposModel;

    private int curPage = 1;
    private final int DEFAULT_PAGE_SIZE = 30;
    private boolean isLoading = false;
    private boolean canLoadMore = false;

    private Map<Integer, Object> initedMap = new HashMap<>();

    final Observer<StatusDataResource> reposStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleReposStatusObserver(statusDataResource);
        }
    };

    public enum ReposType {
        OWNED, STARRED
    }

    private static ReposType mReposType;

    public static BaseFragment newInstance(ReposType reposType) {
        ReposFragment fragment = new ReposFragment();
        mReposType = reposType;
        return fragment;
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
        initedMap.put(1, false);
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.repos_fragment;
    }

    @Override
    public void initViews() {
        initAllView();
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mReposModel = ModelProvider.getModel(this, ReposModel.class);

        showLoading();
        mReposModel.loadRepositories(mReposType, getCurPage());
        mReposModel.getReposStatus().observe(this, reposStatusObserver);
    }

    @Override
    public void onRefresh() {
        initedMap.clear();
        initedMap.put(1, false);
        mReposAdapter.clear();
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

    void initAllView() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshLayoutColors(mContext));

        mReposAdapter = new BaseRecyclerAdapter(mContext);
        mReposAdapter.registerHolder(RepositoryHolder.class, R.layout.layout_item_repository);
        mReposAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                int itemCount = mReposAdapter.getItemCount();
                if (itemCount == 0) {
                    refreshLayout.setVisibility(View.GONE);
                } else {
                    refreshLayout.setVisibility(View.VISIBLE);
                    canLoadMore = itemCount % getPagerSize() == 0;
                    curPage = itemCount % getPagerSize() == 0 ?
                            itemCount / getPagerSize() : (itemCount / getPagerSize()) + 1;
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mReposRecyclerView.setLayoutManager(layoutManager);
        mReposRecyclerView.setAdapter(mReposAdapter);
        mReposRecyclerView.setOnScrollListener(new ScrollListener());
    }

    void initRepositories(List<RepositoryData> repositories) {
        LogUtils.d(TAG, "init adapter data");
        mReposAdapter.setData(repositories);
    }

    void updateRepositories(List<RepositoryData> repositories) {
        LogUtils.d(TAG, "defore upate adapter data = " + mReposAdapter.getItemCount());
        mReposAdapter.addData(repositories);
        LogUtils.d(TAG, "after upate adapter data = " + mReposAdapter.getItemCount());
    }

    void onLoadMore(int page) {
        Object initedByPage = initedMap.get(page);
        if (BaseUtils.isNull(initedByPage)) {
            LogUtils.d(TAG, "current page = " + page + ", has been inited = " + initedByPage);
            initedMap.put(page, false);
            mReposModel.loadRepositories(mReposType, page);
        }
    }

    void showLoading() {
        isLoading = true;
        refreshLayout.setRefreshing(true);
    }

    void hideLoading() {
        isLoading = false;
        refreshLayout.setRefreshing(false);
    }

    int getCurPage() {
        return curPage;
    }

    int getPagerSize() {
        return DEFAULT_PAGE_SIZE;
    }

    class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!canLoadMore || isLoading) return;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            //only LinearLayoutManager can find last visible
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                int lastPosition = linearManager.findLastVisibleItemPosition();
                if (DEBUG)
                    LogUtils.d(TAG, "RecyclerView scrolling, adapter position of the last visible view = " + lastPosition);
                if (lastPosition == mReposAdapter.getItemCount() - 1) {
                    onLoadMore(curPage + 1);
                }
            }
        }
    }
}
