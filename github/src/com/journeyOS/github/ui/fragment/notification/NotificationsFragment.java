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

package com.journeyOS.github.ui.fragment.notification;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.journeyOS.base.adapter.BaseAdapterData;
import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.ToastyUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseListFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.type.NotificationType;
import com.journeyOS.github.ui.fragment.notification.adapter.NotificationData;
import com.journeyOS.github.ui.fragment.notification.adapter.NotificationHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends BaseListFragment implements BaseRecyclerAdapter.HolderClickListener {
    static final String TAG = NotificationsFragment.class.getSimpleName();

    Context mContext;
    NotificationsModel mNotificationsModel;

    Map<Integer, Object> initedMap = new HashMap<>();

    final Observer<StatusDataResource> notificationsStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleReposStatusObserver(statusDataResource);
        }
    };

    static final String EXTRA_NOTIFICATION_TYPE = "notificationType";
    NotificationType mNotificationType = null;

    public static Fragment newInstance(NotificationType type) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_NOTIFICATION_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
        mNotificationType = (NotificationType) getArguments().get(EXTRA_NOTIFICATION_TYPE);
        initedMap.put(1, false);
    }

    @Override
    public void initViews() {

    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mNotificationsModel = ModelProvider.getModel(this, NotificationsModel.class);
        showLoading();

        mNotificationsModel.loadNotifications(mNotificationType, 1, true);
        mNotificationsModel.getNotificationsStatus().observe(this, notificationsStatusObserver);
    }

    @Override
    public Class<? extends BaseViewHolder> attachViewHolder() {
        return NotificationHolder.class;
    }

    @Override
    public int attachViewHolderRes() {
        return R.layout.layout_item_notification;
    }

    @Override
    public void onLoadMore(int page) {
        Object initedByPage = initedMap.get(page);
        if (BaseUtils.isNull(initedByPage)) {
            LogUtils.d(TAG, "current page = " + page + ", has been inited = " + initedByPage);
            showLoading();
            initedMap.put(page, false);

            mNotificationsModel.loadNotifications(mNotificationType, page, true);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mAdapter != null && mAdapter.getData() != null && mAdapter.getData().size() == 0) {
                showToast(ToastyUtils.ToastType.INFO, R.string.no_notifications_tip, false);
            }
        }
    }

    void handleReposStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                hideLoading();
                int page = (Integer) statusDataResource.subData;
                Object initedByPage = initedMap.get(page);
                LogUtils.d(TAG, "(onChanged)current page = " + page + ", has been inited = " + initedByPage);
                if (!BaseUtils.isNull(initedByPage) && (!(Boolean) initedByPage)) {
                    initedMap.put(page, true);
                    if (page == 1) {
                        initNotifications((List<NotificationData>) statusDataResource.data);
                    } else {
                        updateNotifications((List<NotificationData>) statusDataResource.data);
                    }
                }
                break;
            case ERROR:
                hideLoading();
                showTipDialog(statusDataResource.message);
                break;
        }
    }

    void initNotifications(List<NotificationData> notifications) {
        LogUtils.d(TAG, "init adapter data, notifications size = " + notifications.size());
//        if (notifications.size() == 0) {
//            showToast(ToastyUtils.ToastType.INFO, R.string.no_notifications_tip, false);
//        }
        mAdapter.setData(notifications);
        mAdapter.setOnHolderClickListener(this);
    }

    void updateNotifications(List<NotificationData> notifications) {
        LogUtils.d(TAG, "defore upate adapter data = " + mAdapter.getItemCount());
        mAdapter.addData(notifications);
        LogUtils.d(TAG, "after upate adapter data = " + mAdapter.getItemCount());
    }


    @Override
    public void onHolderClicked(int position, BaseAdapterData data) {

    }
}
