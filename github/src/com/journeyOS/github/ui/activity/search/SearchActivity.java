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

package com.journeyOS.github.ui.activity.search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.journeyOS.base.Constant;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.ToastyUtils;
import com.journeyOS.core.Messages;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.adapter.MainPageAdapter;
import com.journeyOS.github.ui.fragment.repos.ReposFragment;
import com.journeyOS.github.ui.fragment.user.UserFragment;
import com.journeyOS.literouter.Router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements MenuItemCompat.OnActionExpandListener,
        SearchView.OnQueryTextListener {
    static final String TAG = SearchActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    MainPageAdapter mAdapter;

    @BindView(R.id.language)
    AppCompatImageView mChoiceLanguage;

    String mQuery;

    public static void show(@NonNull Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    private final Map<Integer, List<Integer>> MENU_ID_MAP = new HashMap<>();

    private boolean isInputMode = true;
    private List<SearchFilter> searchFilters;
    private String[] sortInfos;

    @Override
    public void initBeforeView() {
        super.initBeforeView();

        MENU_ID_MAP.put(0, SearchFilter.REPO_SORT_ID_LIST);
        MENU_ID_MAP.put(1, SearchFilter.USER_SORT_ID_LIST);

        searchFilters = new ArrayList<>();
        searchFilters.add(new SearchFilter(SearchFilter.SearchType.Repository));
        searchFilters.add(new SearchFilter(SearchFilter.SearchType.User));
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.search_activity;
    }

    @Override
    public void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sortInfos = new String[]{
                getString(R.string.best_match), getString(R.string.best_match)
        };

        mAdapter = new MainPageAdapter(this, getSupportFragmentManager());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        if (isInputMode) {
            MenuItemCompat.expandActionView(searchItem);
        } else {
            MenuItemCompat.collapseActionView(searchItem);
        }
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            filterSortMenu(item);
        } else if (SearchFilter.SORT_ID_LIST.contains(item.getItemId())) {
            int page = mViewPager.getCurrentItem();
            postSearchEvent(getSortFilters(page, item.getItemId()));
            sortInfos[page] = item.getTitle().toString();
            setSubTitle(page);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isInputMode) {
            menu.findItem(R.id.action_info).setVisible(true);
            menu.findItem(R.id.action_sort).setVisible(false);
        } else {
            menu.findItem(R.id.action_info).setVisible(false);
            menu.findItem(R.id.action_sort).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        isInputMode = true;
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        isInputMode = false;
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (BaseUtils.isBlank(query)) {
            showToast(ToastyUtils.ToastType.ERROR, R.string.invalid_query, false);
            return true;
        }
        isInputMode = false;
        invalidateOptionsMenu();
        mQuery = query;
        search(query);
        setSubTitle(mViewPager.getCurrentItem());
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void filterSortMenu(MenuItem item) {
        int index = mViewPager.getCurrentItem();
        List<Integer> idList = MENU_ID_MAP.get(index);
        for (Integer id : SearchFilter.SORT_ID_LIST) {
            item.getSubMenu().findItem(id).setVisible(idList.contains(id));
        }
    }

    void setSubTitle(int page) {
        String title = searchFilters.get(0).getQuery();
        if (title.contains(Constant.LANGUAGE_PREFIX)) {
            title = title.substring(0, title.indexOf("+"));
        }
        mToolbar.setSubtitle(title + "/" + sortInfos[page]);
    }

    List<SearchFilter> getQueryFilters(@NonNull String query) {
        for (SearchFilter searchModel : searchFilters) {
            searchModel.setQuery(query);
        }
        return searchFilters;
    }

    SearchFilter getSortFilters(int page, int sortId) {
        return searchFilters.get(page).setSortId(sortId);
    }

    void search(String query) {
        if (mAdapter.getCount() == 0) {
            setupViewPager(getQueryFilters(query));
        } else {
            for (SearchFilter searchFilter : getQueryFilters(query)) {
                postSearchEvent(searchFilter);
            }
        }
    }

    void setupViewPager(List<SearchFilter> searchFilters) {
        Pair<Fragment, Integer> profileInfoFragmentPair = new Pair<>(ReposFragment.newInstanceForSearch(searchFilters.get(0)), R.string.repositories);
        mAdapter.addFrag(profileInfoFragmentPair);

        Pair<Fragment, Integer> userFragmentPair = new Pair<>(UserFragment.newInstanceForSearch(searchFilters.get(1)), R.string.user);
        mAdapter.addFrag(userFragmentPair);

        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setVisibility(View.VISIBLE);

        for (int index = 0; index < mAdapter.getCount(); index++) {
            mTabLayout.getTabAt(index).setCustomView(mAdapter.getTabView(index, mTabLayout));
        }

        mViewPager.setOffscreenPageLimit(mAdapter.getCount());
        mViewPager.setCurrentItem(0);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mChoiceLanguage.setVisibility(tab.getPosition() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    void postSearchEvent(SearchFilter searchFilter) {
        LogUtils.d(LogUtils.TAG, "postSearchEvent = " + searchFilter);
        Messages msg = new Messages();
        msg.what = Messages.MSG_SEARCHING;
        msg.obj = searchFilter;
        Router.getDefault().post(msg);
    }

    @OnClick(R.id.language)
    public void onLanguageClick() {
        LogUtils.d(TAG, "on language click");
        final String[] items = getResources().getStringArray(R.array.language);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.search_language)
                .setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //https://api.github.com/search/repositories?q=tetris+language:assembly&sort=stars&order=desc
                        String language = Constant.LANGUAGE_PREFIX + items[i];
                        search(mQuery + language);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();

        dialog.show();
    }
}
