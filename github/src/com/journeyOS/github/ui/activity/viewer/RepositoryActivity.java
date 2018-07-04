/*
 *
 *  * Copyright (c) 2018 anqi.huang@outlook.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.journeyOS.github.ui.activity.viewer;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.ToastyUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.type.RepoType;
import com.journeyOS.github.ui.adapter.MainPageAdapter;
import com.journeyOS.github.ui.fragment.files.RepoFilesFragment;
import com.journeyOS.github.ui.fragment.info.RepoInfoFragment;
import com.journeyOS.github.ui.fragment.repos.ReposModel;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RepositoryActivity extends BaseActivity {
    static final String TAG = RepositoryActivity.class.getSimpleName();
    static final String EXTRA_REPOSITORY_DATA = "repositoryData";
    static final String EXTRA_USER = "user";
    static final String EXTRA_REPO = "repo";

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    MainPageAdapter mAdapter;

    boolean isStarred;
    boolean isStarredInited = false;
    boolean isWatched;
    boolean isWatchedInited = false;
    Application mContext;
    RepositoryData mRepositoryData;
    RepositoryModel mRepositoryModel;
    ReposModel mReposModel;

    String mUser;
    String mRepo;
    Map<Integer, Object> initedMap = new HashMap<>();

    final Observer<StatusDataResource> starredStatusStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleStarredStatusObserver(statusDataResource);
        }
    };

    final Observer<StatusDataResource> watchedStatusStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleWatchedStatusObserver(statusDataResource);
        }
    };

    public static void show(@NonNull Context context, @NonNull RepositoryData repositoryData) {
        Intent intent = new Intent(context, RepositoryActivity.class);
        intent.putExtra(EXTRA_REPOSITORY_DATA, repositoryData);
        context.startActivity(intent);
    }

    public static void show(@NonNull Context context, @NonNull String owner,
                            @NonNull String repoName) {
        Intent intent = new Intent(context, RepositoryActivity.class);
        intent.putExtra(EXTRA_USER, owner);
        intent.putExtra(EXTRA_REPO, repoName);
        context.startActivity(intent);
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.repository_activity;
    }

    @Override
    public void initViews() {
        mRepositoryData = getIntent().getParcelableExtra(EXTRA_REPOSITORY_DATA);
        if (!BaseUtils.isNull(mRepositoryData)) {
            initView();
        } else {
            mUser = getIntent().getStringExtra(EXTRA_USER);
            mRepo = getIntent().getStringExtra(EXTRA_REPO);
            initedMap.put(1, false);
        }
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        if (!BaseUtils.isNull(mRepositoryData)) {
            initRepositoryData();
        } else {
            mReposModel = ModelProvider.getModel(this, ReposModel.class);
            mReposModel.loadRepositories(RepoType.OWNED, BaseUtils.isNull(mUser) ? "" : mUser, 1);
            mReposModel.getReposStatus().observe(this, new Observer<StatusDataResource>() {
                @Override
                public void onChanged(@Nullable StatusDataResource statusDataResource) {
                    switch (statusDataResource.status) {
                        case SUCCESS:
                            int page = (Integer) statusDataResource.subData;
                            Object initedByPage = initedMap.get(page);
                            LogUtils.d(TAG, "(onChanged)current page = " + page + ", has been inited = " + initedByPage);
                            if (!BaseUtils.isNull(initedByPage) && (!(Boolean) initedByPage)) {
                                initedMap.put(page, true);
                                if (page == 1) {
                                    handleReposStatusObserver((List<RepositoryData>) statusDataResource.data);
                                }
                            }
                            break;
                        case ERROR:
                            break;
                    }
                }
            });
        }
    }

    void handleReposStatusObserver(List<RepositoryData> repositoryDataList) {
        for (RepositoryData repositoryData : repositoryDataList) {
            LogUtils.d(TAG, "handleReposStatusObserver = " + repositoryData.toString());
            if (repositoryData.fullName.equals(mUser + "/" + mRepo)) {
                mRepositoryData = repositoryData;
                initView();
                initRepositoryData();
            }
        }
    }

    void initView() {
        mCollapsingToolbarLayout.setTitleEnabled(false);
        mToolbar.setTitle(mRepositoryData.name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager(mRepositoryData);
    }

    void initRepositoryData() {
        mRepositoryModel = ModelProvider.getModel(this, RepositoryModel.class);

        mRepositoryModel.checkStarred(mRepositoryData.owner.login, mRepositoryData.name);
        mRepositoryModel.getStarredStatus().observe(this, starredStatusStatusObserver);

        mRepositoryModel.checkWatched(mRepositoryData.owner.login, mRepositoryData.name);
        mRepositoryModel.getWatchedStatus().observe(this, watchedStatusStatusObserver);
    }

    void setupViewPager(RepositoryData repositoryData) {
        mAdapter = new MainPageAdapter(this, getSupportFragmentManager());

        //add more fragment there.
        Pair<Fragment, Integer> infoFragmentPair = new Pair<>(RepoInfoFragment.newInstance(repositoryData), R.string.info);
        mAdapter.addFrag(infoFragmentPair);

        Pair<Fragment, Integer> fileFragmentPair = new Pair<>(RepoFilesFragment.newInstance(repositoryData), R.string.files);
        mAdapter.addFrag(fileFragmentPair);

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        for (int index = 0; index < mAdapter.getCount(); index++) {
            mTabLayout.getTabAt(index).setCustomView(mAdapter.getTabView(index, mTabLayout));
        }

        mViewPager.setOffscreenPageLimit(mAdapter.getCount());
        mViewPager.setCurrentItem(mAdapter.getCount() / 2);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = mAdapter.getCurFragment();
        if (fragment != null
                && fragment instanceof IFragmentKeyListener
                && ((IFragmentKeyListener) fragment).onKeyDown(keyCode, event)) {
            return true;
        }
        return onMainKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_repository, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem starItem = menu.findItem(R.id.action_star);
        starItem.setTitle(isStarred ? R.string.unstar : R.string.star);
        starItem.setIcon(isStarred ?
                R.drawable.svg_star_title : R.drawable.svg_un_star_title);
        menu.findItem(R.id.action_watch).setTitle(isWatched ?
                R.string.unwatch : R.string.watch);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BaseUtils.isNull(mRepositoryData) && item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.action_star:
                isStarredInited = true;
                mRepositoryModel.starRepo(mRepositoryData.owner.login, mRepositoryData.name, !isStarred);
                return true;
            case R.id.action_share:
                BaseUtils.shareText(this, mRepositoryData.htmlUrl);
                return true;
            case R.id.action_open_in_browser:
                BaseUtils.openInBrowser(this, mRepositoryData.htmlUrl);
                return true;
            case R.id.action_copy_url:
                BaseUtils.copyToClipboard(this, mRepositoryData.htmlUrl);
                return true;
            case R.id.action_watch:
                isWatchedInited = true;
                mRepositoryModel.watchRepo(mRepositoryData.owner.login, mRepositoryData.name, !isWatched);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean onMainKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public interface IFragmentKeyListener {
        boolean onKeyDown(int keyCode, KeyEvent event);
    }

    void handleStarredStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                isStarred = (boolean) statusDataResource.data;
                LogUtils.d(TAG, "handle starred status, isStarred = " + isStarred);
                invalidateOptionsMenu();
                if (isStarredInited) {
                    isStarredInited = false;
                    showToast(ToastyUtils.ToastType.SUCCESS, isStarred ? R.string.starred : R.string.unstarred, false);
                }
                break;
            case ERROR:
                showToast(ToastyUtils.ToastType.ERROR, statusDataResource.message, false);
                break;
        }
    }

    void handleWatchedStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                isWatched = (boolean) statusDataResource.data;
                LogUtils.d(TAG, "handle watch status, isWatched = " + isWatched);
                invalidateOptionsMenu();
                if (isWatchedInited) {
                    isWatchedInited = false;
                    showToast(ToastyUtils.ToastType.SUCCESS, isWatched ? R.string.watched : R.string.unwatched, false);
                }
                break;
            case ERROR:
                showToast(ToastyUtils.ToastType.ERROR, statusDataResource.message, false);
                break;
        }
    }
}
