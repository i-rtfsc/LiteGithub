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

package com.journeyOS.github.ui.viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.adapter.MainPageAdapter;
import com.journeyOS.github.ui.fragment.files.RepoFilesFragment;

import butterknife.BindView;

public class RepositoryActivity extends BaseActivity {
    static final String EXTRA_LOGIN = "login";
    static final String EXTRA_NAME = "name";
    static final String EXTRA_DEFAULT_BRANCH = "defaultBranch";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    MainPageAdapter mAdapter;

    public static void show(@NonNull Context context, @NonNull String login, @NonNull String name, @NonNull String defaultBranch) {
        Intent intent = new Intent(context, RepositoryActivity.class);
        intent.putExtra(EXTRA_LOGIN, login);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_DEFAULT_BRANCH, defaultBranch);
        context.startActivity(intent);
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.repository_activity;
    }

    @Override
    public void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager();
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
    }

    void setupViewPager() {
        String login = getIntent().getStringExtra(EXTRA_LOGIN);
        String name = getIntent().getStringExtra(EXTRA_NAME);
        String defaultBranch = getIntent().getStringExtra(EXTRA_DEFAULT_BRANCH);

        mAdapter = new MainPageAdapter(this, getSupportFragmentManager());

        //add more fragment there.

        Pair<BaseFragment, Integer> fileFragmentPair = new Pair<>(RepoFilesFragment.newInstance(login, name, defaultBranch), R.string.files);
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

    protected boolean onMainKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public interface IFragmentKeyListener {
        boolean onKeyDown(int keyCode, KeyEvent event);
    }
}
