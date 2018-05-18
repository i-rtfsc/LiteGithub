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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryData;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryHolder;

import java.util.List;

import butterknife.BindView;

public class ReposFragment extends BaseFragment {

    @BindView(R.id.repos_recyclerView)
    RecyclerView mReposRecyclerView;

    private BaseRecyclerAdapter mReposAdapter;

    private Context mContext;
    private ReposModel mReposModel;

    public enum ReposType {
        OWNED, STARRED
    }

    private static ReposType mReposType;

    public static ReposFragment newInstance(ReposType reposType) {
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

        mReposModel.loadRepositories(mReposType);
        mReposModel.getReposStatus().observe(this, new Observer<StatusDataResource>() {
            @Override
            public void onChanged(@Nullable StatusDataResource statusDataResource) {
                switch (statusDataResource.status) {
                    case SUCCESS:
                        initRepositories((List<RepositoryData>) statusDataResource.data);
                        break;
                    case ERROR:
                        showTipDialog(statusDataResource.message);
                        break;
                }
            }
        });
    }

    void initRepositories(List<RepositoryData> repositories) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mReposRecyclerView.setLayoutManager(layoutManager);
        mReposAdapter = new BaseRecyclerAdapter(mContext);
        mReposAdapter.setData(repositories);
        mReposAdapter.registerHolder(RepositoryHolder.class, R.layout.layout_item_repository);
        mReposRecyclerView.setAdapter(mReposAdapter);
    }
}
