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
import com.journeyOS.base.utils.ViewUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryData;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ReposFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.repos_recyclerView)
    RecyclerView mReposRecyclerView;

    private BaseRecyclerAdapter mReposAdapter;

    private Context mContext;
    private ReposModel mReposModel;

    private List<RepositoryData> mData = new ArrayList<>();

    private RecyclerView.AdapterDataObserver observer;
    private int curPage = 1;
    private final int DEFAULT_PAGE_SIZE = 30;
    private boolean isLoading = false;
    private boolean canLoadMore = false;

    private boolean inited = false;

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
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.repos_fragment;
    }

    @Override
    public void initViews() {

    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mReposModel = ModelProvider.getModel(this, ReposModel.class);

        showLoading();
        mReposModel.loadRepositories(mReposType, getCurPage());
        mReposModel.getReposStatus().observe(this, new Observer<StatusDataResource>() {
            @Override
            public void onChanged(@Nullable StatusDataResource statusDataResource) {
                switch (statusDataResource.status) {
                    case SUCCESS:
                        hideLoading();
                        if (!inited) {
                            initRepositories((List<RepositoryData>) statusDataResource.data);
                        } else {
                            updateRepositories((List<RepositoryData>) statusDataResource.data);
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showTipDialog(statusDataResource.message);
                        break;
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        inited = false;
        mReposAdapter.clear();
        showLoading();
        onLoadMore(1);
    }

    void initRepositories(List<RepositoryData> repositories) {
        mData.addAll(repositories);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshLayoutColors(mContext));

        mReposAdapter = new BaseRecyclerAdapter(mContext);
        mReposAdapter.setData(repositories);
        mReposAdapter.registerHolder(RepositoryHolder.class, R.layout.layout_item_repository);
        observer = new RecyclerView.AdapterDataObserver() {
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
        };
        mReposAdapter.registerAdapterDataObserver(observer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mReposRecyclerView.setLayoutManager(layoutManager);
        mReposRecyclerView.setAdapter(mReposAdapter);
        mReposRecyclerView.setOnScrollListener(new ScrollListener());
        inited = true;
    }

    void updateRepositories(List<RepositoryData> repositories) {
        mData.removeAll(repositories);
        mReposAdapter.setData(repositories);
    }

    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!canLoadMore || isLoading) return;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            //only LinearLayoutManager can find last visible
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                int lastPosition = linearManager.findLastVisibleItemPosition();
                if (lastPosition == mReposAdapter.getItemCount() - 1) {
                    onLoadMore(curPage + 1);
                }
            }
        }
    }

    void onLoadMore(int page) {
        mReposModel.loadRepositories(mReposType, page);
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
}
