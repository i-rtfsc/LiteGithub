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

import androidx.lifecycle.Observer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.stetho.common.LogUtil;
import com.journeyOS.base.adapter.BaseAdapterData;
import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.Messages;
import com.journeyOS.core.base.BaseListFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.type.UserType;
import com.journeyOS.github.ui.activity.profile.ProfileActivity;
import com.journeyOS.github.ui.activity.search.SearchFilter;
import com.journeyOS.github.ui.fragment.user.adapter.UserData;
import com.journeyOS.github.ui.fragment.user.adapter.UserHolder;
import com.journeyOS.literouter.RouterListener;
import com.journeyOS.literouter.RouterMsssage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFragment extends BaseListFragment implements RouterListener, BaseRecyclerAdapter.HolderClickListener {
    static final String TAG = UserFragment.class.getSimpleName();

    static final String EXTRA_USER_TYPE = "usersType";
    static final String EXTRA_USER = "user";
    static final String EXTRA_REPO = "repo";
    UserType mUserType;
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

    public static BaseListFragment newInstance(@NonNull UserType usersType, @NonNull String user,
                                               @NonNull String repo) {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_USER_TYPE, usersType);
        bundle.putString(EXTRA_USER, user);
        bundle.putString(EXTRA_REPO, repo);
        fragment.setArguments(bundle);
        return fragment;
    }


    static final String EXTRA_SEARCH_FILTER = "searchFilter";
    SearchFilter mSearchFilter = null;

    public static Fragment newInstanceForSearch(@NonNull SearchFilter searchFilter) {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SEARCH_FILTER, searchFilter);
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
        mUserType = (UserType) getArguments().get(EXTRA_USER_TYPE);
        mUser = getArguments().getString(EXTRA_USER);
        mRepo = getArguments().getString(EXTRA_REPO);

        mSearchFilter = getArguments().getParcelable(EXTRA_SEARCH_FILTER);
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mUserModel = ModelProvider.getModel(this, UserModel.class);

        if (!BaseUtils.isNull(mUserType)) {
            mUserModel.loadUsers(mUserType, mUser, mRepo, 1, true);
            mUserModel.getUsersStatus().observe(this, reposStatusObserver);
        }

        if (!BaseUtils.isNull(mSearchFilter)) {
            mUserModel.searchUsers(mSearchFilter, getCurPage());
            mUserModel.getSearchUsersStatus().observe(this, reposStatusObserver);
        }

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

            if (!BaseUtils.isNull(mUserType)) {
                mUserModel.loadUsers(mUserType, mUser, mRepo, page, true);
            }

            if (!BaseUtils.isNull(mSearchFilter)) {
                mUserModel.searchUsers(mSearchFilter, page);
            }
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
        mAdapter.setOnHolderClickListener(this);
    }

    void updateUsers(List<UserData> userData) {
        LogUtils.d(TAG, "defore upate adapter data = " + mAdapter.getItemCount());
        mAdapter.addData(userData);
        LogUtils.d(TAG, "after upate adapter data = " + mAdapter.getItemCount());
    }

    @Override
    public void onShowMessage(RouterMsssage message) {
        Messages msg = (Messages) message;
        LogUtils.d(LogUtils.TAG, "postSearchEvent = " + msg.what + " , SearchFilter = " + msg.obj);
        switch (msg.what) {
            case Messages.MSG_SEARCHING:
                if (!BaseUtils.isNull(mSearchFilter)) {
                    initedMap.clear();
                    initedMap.put(1, false);
                    mAdapter.clear();
                    showLoading();
                    mSearchFilter = (SearchFilter) msg.obj;
                    mUserModel.searchUsers(mSearchFilter, 1);
                }
                break;
        }
    }

    @Override
    public void onHolderClicked(int position, BaseAdapterData data) {
        LogUtil.d(TAG, "user item has been click");
        ProfileActivity.show(CoreManager.getContext(), ((UserData) data).login);
    }
}
