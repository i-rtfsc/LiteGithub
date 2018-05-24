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

package com.journeyOS.github.ui.main;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.journeyOS.base.Constant;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.UIUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.userprovider.AuthUser;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.MaterialDrawer;
import com.journeyOS.github.MaterialDrawer.OnDrawerItemClickListener;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.repos.ReposFragment;
import com.journeyOS.github.ui.settings.SettingsActivity;
import com.mikepenz.materialdrawer.Drawer;

import butterknife.BindView;

public class GithubActivity extends BaseActivity implements OnDrawerItemClickListener {
    private static final String TAG = GithubActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frame_container)
    FrameLayout mFrameLayoutContent;

    GithubModel mGithubModel;
    Context mContext;

    private MaterialDrawer mMaterialDrawer;
    private Drawer result = null;

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.github_activity;
    }

    @Override
    public void initViews() {
        UIUtils.setStatusBarColor(this, this.getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);
        mMaterialDrawer = new MaterialDrawer(this);
        mMaterialDrawer.setOnDrawerItemClickListener(this);
    }


    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        final Bundle bundle = savedInstanceState;
        mGithubModel = ModelProvider.getModel(this, GithubModel.class);
        mGithubModel.searchAuthUser();
        mGithubModel.getAuthUserStatus().observe(this, new Observer<AuthUser>() {
            @Override
            public void onChanged(@Nullable final AuthUser user) {
                CoreManager.setAccessToken(user.accessToken);
                result = mMaterialDrawer.initDrawer(bundle, user, mToolbar);
                loadFragment(ReposFragment.newInstance(ReposFragment.ReposType.OWNED));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
//        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    @Override
    public void handleDrawerItem(String tag, boolean longClick) {
        LogUtils.d(TAG, "handle drawer item = " + tag + " , is long click = " + longClick);
        switch (tag) {
            case Constant.MENU_PROFILE:
                mToolbar.setTitle(R.string.profile);
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
            case Constant.MENU_REPOS:
                mToolbar.setTitle(R.string.my_repos);
                loadFragment(ReposFragment.newInstance(ReposFragment.ReposType.OWNED));
                break;
            case Constant.MENU_NOTIFICATION:
                mToolbar.setTitle(R.string.notifications);
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
            case Constant.MENU_ISSUE:
                mToolbar.setTitle(R.string.issues);
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
            case Constant.MENU_SEARCH:
                mToolbar.setTitle(R.string.search);
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
            case Constant.MENU_STARRED:
                mToolbar.setTitle(R.string.starred);
                loadFragment(ReposFragment.newInstance(ReposFragment.ReposType.STARRED));
                break;
            case Constant.MENU_SETTINGS:
                SettingsActivity.newInstance(mContext);
                break;
            case Constant.MENU_ABOUT:
                mToolbar.setTitle(R.string.about);
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
        }
    }
}
