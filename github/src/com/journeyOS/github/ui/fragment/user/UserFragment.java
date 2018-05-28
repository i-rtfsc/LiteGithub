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

package com.journeyOS.github.ui.fragment.user;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.base.BaseListFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.user.adapter.UserData;
import com.journeyOS.github.ui.fragment.user.adapter.UserHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFragment extends BaseListFragment {
    static final String TAG = UserFragment.class.getSimpleName();

    public enum UsersType {
        STARGAZERS, WATCHERS, FOLLOWERS, FOLLOWING
    }

    static final String EXTRA_USER_TYPE = "usersType";
    static final String EXTRA_USER = "user";
    static final String EXTRA_REPO = "repo";
    UserFragment.UsersType mUsersType;
    String mUser;
    String mRepo;

    UserModel mUserModel;

    Map<Integer, Object> initedMap = new HashMap<>();

    final Observer<StatusDataResource> reposStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleUserStatusObserver(statusDataResource);
        }
    };

    public static BaseListFragment newInstance(@NonNull UserFragment.UsersType usersType, @NonNull String user,
                                               @NonNull String repo) {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_USER_TYPE, usersType);
        bundle.putString(EXTRA_USER, user);
        bundle.putString(EXTRA_REPO, repo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        initedMap.put(1, false);
    }

    @Override
    public void initViews() {
        mUsersType = (UserFragment.UsersType) getArguments().get(EXTRA_USER_TYPE);
        mUser = getArguments().getString(EXTRA_USER);
        mRepo = getArguments().getString(EXTRA_REPO);
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mUserModel = ModelProvider.getModel(this, UserModel.class);
        mUserModel.loadUsers(mUsersType, mUser, mRepo, 1, true);

        mUserModel.getUsersStatus().observe(this, reposStatusObserver);
    }

    @Override
    public Class<? extends BaseViewHolder> attachViewHolder() {
        return UserHolder.class;
    }

    @Override
    public int attachViewHolderRes() {
        return R.layout.layout_item_user;
    }

    @Override
    public void onLoadMore(int page) {
        Object initedByPage = initedMap.get(page);
        if (BaseUtils.isNull(initedByPage)) {
            LogUtils.d(TAG, "current page = " + page + ", has been inited = " + initedByPage);
            showLoading();
            initedMap.put(page, false);
            mUserModel.loadUsers(mUsersType, mUser, mRepo, page, true);
        }
    }

    @Override
    public void onReLoadData() {
        initedMap.clear();
        initedMap.put(1, false);
        mAdapter.clear();
        showLoading();
        onLoadMore(1);
    }

    void handleUserStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                hideLoading();
                int page = (Integer) statusDataResource.subData;
                Object initedByPage = initedMap.get(page);
                LogUtils.d(TAG, "(onChanged)current page = " + page + ", has been inited = " + initedByPage);
                if (!BaseUtils.isNull(initedByPage) && (!(Boolean) initedByPage)) {
                    initedMap.put(page, true);
                    if (page == 1) {
                        initUsers((List<UserData>) statusDataResource.data);
                    } else {
                        updateUsers((List<UserData>) statusDataResource.data);
                    }
                }
                break;
            case ERROR:
                hideLoading();
                showTipDialog(statusDataResource.message);
                break;
        }
    }

    void initUsers(List<UserData> userData) {
        LogUtils.d(TAG, "init adapter data");
        mAdapter.setData(userData);
    }

    void updateUsers(List<UserData> userData) {
        LogUtils.d(TAG, "defore upate adapter data = " + mAdapter.getItemCount());
        mAdapter.addData(userData);
        LogUtils.d(TAG, "after upate adapter data = " + mAdapter.getItemCount());
    }
}
