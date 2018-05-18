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
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.journeyOS.base.Constant;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.UIUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.userprovider.AuthUser;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.repos.ReposFragment;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class GithubActivity extends BaseActivity {
    private static final String TAG = GithubActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frame_container)
    FrameLayout mFrameLayoutContent;

    GithubModel mGithubModel;
    Context mContext;

    private AccountHeader headerResult = null;
    private Drawer result = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;

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
                initDrawer(bundle, user);
                loadFragment(ReposFragment.newInstance(ReposFragment.ReposType.OWNED));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
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

    void initDrawer(Bundle savedInstanceState, AuthUser user) {

        ImageView imageView = new ImageView(mContext);
        Picasso.with(mContext)
                .load(Uri.parse(user.avatar))
                .placeholder(R.mipmap.user)
                .into(imageView);

        final IProfile profile = new ProfileDrawerItem()
                .withName(user.name)
                .withEmail(user.email)
                .withIcon(imageView.getDrawable());

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .addProfiles(profile,
                        new ProfileSettingDrawerItem().withName(R.string.add_account).withDescription(R.string.add_new_account).withIcon(R.drawable.svg_add_user).withIdentifier(9),
                        new ProfileSettingDrawerItem().withName(R.string.manage_account).withIcon(R.drawable.svg_menu_settings))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        LogUtils.d(TAG, "onProfileChanged() called with: view = [" + view + "], profile = [" + profile + "], current = [" + current + "]");
                        return false;
                    }
                })
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        LogUtils.d(TAG, "onClick() called with: view = [" + view + "], profile = [" + profile + "]");
                        return false;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withHasStableIds(true)
                .withDrawerLayout(R.layout.crossfade_drawer)
                .withDrawerWidthDp(72)
                .withActionBarDrawerToggleAnimated(true)
                .withGenerateMiniDrawer(true)
                .withAccountHeader(headerResult)
//                .withRootView(R.id.drawer_container)
                .withDisplayBelowStatusBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.profile).withIcon(R.drawable.svg_menu_user).withTag(Constant.MENU_PROFILE),
                        new PrimaryDrawerItem().withIdentifier(2).withName(R.string.my_repos).withIcon(R.drawable.svg_menu_repos).withTag(Constant.MENU_REPOS),
                        new PrimaryDrawerItem().withIdentifier(3).withName(R.string.notifications).withIcon(R.drawable.svg_menu_notification).withTag(Constant.MENU_NOTIFICATION),
                        new PrimaryDrawerItem().withIdentifier(4).withName(R.string.issues).withIcon(R.drawable.svg_menu_issue).withTag(Constant.MENU_ISSUE),
                        new SectionDrawerItem().withName(R.string.advanced),
                        new SecondaryDrawerItem().withIdentifier(5).withName(R.string.search).withIcon(R.drawable.svg_menu_search).withTag(Constant.MENU_SEARCH),
                        new SecondaryDrawerItem().withIdentifier(6).withName(R.string.starred).withIcon(R.drawable.svg_menu_starred).withTag(Constant.MENU_STARRED)
                )
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withIdentifier(7).withName(R.string.settings).withIcon(R.drawable.svg_menu_settings).withTag(Constant.MENU_SETTINGS),
                        new SecondaryDrawerItem().withIdentifier(8).withName(R.string.about).withIcon(R.drawable.svg_menu_about).withTag(Constant.MENU_ABOUT)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (result != null && result.isDrawerOpen()) {
                            result.closeDrawer();
                        }
                        String tag = (String) drawerItem.getTag();
                        handleDrawerItem(tag, false);
                        return false;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        if (result != null && result.isDrawerOpen()) {
                            result.closeDrawer();
                        }
                        String tag = (String) drawerItem.getTag();
                        handleDrawerItem(tag, true);
                        return false;
                    }
                })
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();

        crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(mContext));
        final MiniDrawer miniResult = result.getMiniDrawer();
        View view = miniResult.build(mContext);
        view.setBackgroundColor(mContext.getResources().getColor(R.color.darkslategray));
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });

        mFrameLayoutContent.setBackgroundColor(mContext.getResources().getColor(R.color.main_background));
    }

    void handleDrawerItem(String tag, boolean longClick) {
        LogUtils.d(TAG, "handle drawer item = " + tag + " , is long click = " + longClick);
        switch (tag) {
            case Constant.MENU_PROFILE:
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
            case Constant.MENU_REPOS:
                loadFragment(ReposFragment.newInstance(ReposFragment.ReposType.OWNED));
                break;
            case Constant.MENU_NOTIFICATION:
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
            case Constant.MENU_ISSUE:
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
            case Constant.MENU_SEARCH:
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
            case Constant.MENU_STARRED:
                loadFragment(ReposFragment.newInstance(ReposFragment.ReposType.STARRED));
                break;
            case Constant.MENU_SETTINGS:
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
            case Constant.MENU_ABOUT:
                showShortToast(ToastType.INFO, mContext.getString(R.string.developing));
                break;
        }
    }

    void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }
}
