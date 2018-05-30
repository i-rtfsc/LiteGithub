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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.adapter.MainPageAdapter;
import com.journeyOS.github.ui.fragment.files.RepoFilesFragment;
import com.journeyOS.github.ui.fragment.info.RepoInfoFragment;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryData;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;

public class RepositoryActivity extends BaseActivity {
    static final String EXTRA_REPOSITORY_DATA = "repositoryData";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    MainPageAdapter mAdapter;

    Application mContext;

    public static void show(@NonNull Context context, @NonNull RepositoryData repositoryData) {
        Intent intent = new Intent(context, RepositoryActivity.class);
        intent.putExtra(EXTRA_REPOSITORY_DATA, repositoryData);
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
        RepositoryData repositoryData = getIntent().getParcelableExtra(EXTRA_REPOSITORY_DATA);

        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager(repositoryData);
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
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

    protected boolean onMainKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public interface IFragmentKeyListener {
        boolean onKeyDown(int keyCode, KeyEvent event);
    }
}
