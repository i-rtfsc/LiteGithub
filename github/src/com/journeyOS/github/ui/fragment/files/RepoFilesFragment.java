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

package com.journeyOS.github.ui.fragment.files;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.ToastyUtils;
import com.journeyOS.base.utils.ViewUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.Messages;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.activity.viewer.RepositoryActivity;
import com.journeyOS.github.ui.activity.viewer.ViewerActivity;
import com.journeyOS.github.ui.fragment.files.adapter.ReposFileData;
import com.journeyOS.github.ui.fragment.files.adapter.ReposFileHolder;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryData;
import com.journeyOS.literouter.RouterListener;
import com.journeyOS.literouter.RouterMsssage;

import java.util.List;

import butterknife.BindView;

public class RepoFilesFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, RouterListener,
        RepositoryActivity.IFragmentKeyListener {

    static final String TAG = RepoFilesFragment.class.getSimpleName();

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mReposFilesRecyclerView;

    BaseRecyclerAdapter mReposFilesAdapter;
    Context mContext;
    ReposFileModel mReposFileModel;

    static final String EXTRA_REPOSITORY_DATA = "repositoryData";
    RepositoryData mRepositoryData;
    String curPath = "";

    final Observer<StatusDataResource> reposFilesStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleReposFilesStatusObserver(statusDataResource);
        }
    };

    public static Fragment newInstance(@NonNull RepositoryData repositoryData) {
        RepoFilesFragment fragment = new RepoFilesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_REPOSITORY_DATA, repositoryData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.repos_files_fragment;
    }

    @Override
    public void initViews() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshLayoutColors(getContext()));

        mRepositoryData = getArguments().getParcelable(EXTRA_REPOSITORY_DATA);
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mReposFileModel = ModelProvider.getModel(this, ReposFileModel.class);

        showLoading();
        mReposFileModel.loadFiles(mRepositoryData.owner.login, mRepositoryData.name, mRepositoryData.defaultBranch, curPath, false);

        mReposFileModel.getReposFilesStatus().observe(this, reposFilesStatusObserver);
    }

    void handleReposFilesStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                hideLoading();
                initReposfiles((List<ReposFileData>) statusDataResource.data);
                break;
            case ERROR:
                hideLoading();
                showTipDialog(statusDataResource.message);
                break;
        }
    }

    void initReposfiles(List<ReposFileData> reposFileData) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mReposFilesRecyclerView.setLayoutManager(layoutManager);
        mReposFilesAdapter = new BaseRecyclerAdapter(mContext);
        mReposFilesAdapter.setData(reposFileData);
        mReposFilesAdapter.registerHolder(ReposFileHolder.class, R.layout.layout_item_file);
        mReposFilesRecyclerView.setAdapter(mReposFilesAdapter);
    }

    @Override
    public void onRefresh() {
        mReposFileModel.loadFiles(mRepositoryData.owner.login, mRepositoryData.name, mRepositoryData.defaultBranch, curPath, false);
    }

    @Override
    public void onShowMessage(RouterMsssage message) {
        Messages msg = (Messages) message;
        switch (msg.what) {
            case Messages.MSG_FILE_ITEM_CILCKED:
                ReposFileData data = (ReposFileData) msg.obj;
                LogUtils.d(TAG, "is this file is folder = " + data.isDir);
                if (data.isDir) {
                    showLoading();
                    curPath = data.path;
                    mReposFileModel.loadFiles(mRepositoryData.owner.login, mRepositoryData.name, mRepositoryData.defaultBranch, curPath, false);
                } else {
                    if (data.size == 0) {
                        showToast(ToastyUtils.ToastType.WARNING, R.string.sub_modules, false);
                    } else {
                        ViewerActivity.show(getActivity(), data);
                    }
                }
                break;
        }

    }

    void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    void hideLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!BaseUtils.isBlank(curPath)) {
                curPath = curPath.contains("/") ?
                        curPath.substring(0, curPath.lastIndexOf("/")) : "";
                mReposFileModel.loadFiles(mRepositoryData.owner.login, mRepositoryData.name, mRepositoryData.defaultBranch, curPath, false);
                return true;
            }
        }
        return false;
    }
}
