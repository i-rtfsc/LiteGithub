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

package com.journeyOS.github.ui.activity.profile;

import android.app.Application;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.core.util.Pair;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.User;
import com.journeyOS.github.type.RepoType;
import com.journeyOS.github.ui.adapter.MainPageAdapter;
import com.journeyOS.github.ui.fragment.profile.ProfileInfoFragment;
import com.journeyOS.github.ui.fragment.repos.ReposFragment;

import butterknife.BindView;

public class ProfileActivity extends BaseActivity {
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    MainPageAdapter mAdapter;

    Application mContext;
    ProfileModel mProfileModel;

    static final String EXTRA_LOGIN = "login";
    String mLogin;

    final Observer<StatusDataResource> userStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleUserStatusObserver(statusDataResource);
        }
    };

    public static void show(@NonNull Context context, String login) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_LOGIN, login);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.profile_activity;
    }

    @Override
    public void initViews() {
        mLogin = getIntent().getStringExtra(EXTRA_LOGIN);

        mCollapsingToolbarLayout.setTitleEnabled(false);
        mToolbar.setTitle(mLogin);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);

        mProfileModel = ModelProvider.getModel(this, ProfileModel.class);
        mProfileModel.getUserInfo(mLogin);

        mProfileModel.getUserStatus().observe(this, userStatusObserver);
    }

    void handleUserStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                setupViewPager((User) statusDataResource.data);
                break;
            case ERROR:
                showTipDialog(statusDataResource.message);
                break;
        }
    }

    void setupViewPager(User user) {

        mAdapter = new MainPageAdapter(this, getSupportFragmentManager());

        Pair<Fragment, Integer> profileInfoFragmentPair = new Pair<>(ProfileInfoFragment.newInstance(user), R.string.info);
        mAdapter.addFrag(profileInfoFragmentPair);

        Pair<Fragment, Integer> fileFragmentPair = new Pair<>(ReposFragment.newInstance(RepoType.STARRED, user.login, user.name), R.string.starred);
        mAdapter.addFrag(fileFragmentPair);

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        for (int index = 0; index < mAdapter.getCount(); index++) {
            mTabLayout.getTabAt(index).setCustomView(mAdapter.getTabView(index, mTabLayout));
        }

        mViewPager.setOffscreenPageLimit(mAdapter.getCount());
        mViewPager.setCurrentItem(0);
    }
}
