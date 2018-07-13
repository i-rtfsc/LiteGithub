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

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.http.AppHttpClient;
import com.journeyOS.core.http.HttpCoreManager;
import com.journeyOS.core.http.HttpObserver;
import com.journeyOS.core.http.HttpResponse;
import com.journeyOS.core.viewmodel.BaseViewModel;
import com.journeyOS.github.BuildConfig;
import com.journeyOS.github.api.GithubService;
import com.journeyOS.github.entity.Notification;
import com.journeyOS.github.type.NotificationType;
import com.journeyOS.github.ui.fragment.notification.adapter.NotificationData;

import java.util.ArrayList;

import retrofit2.Response;
import rx.Observable;

public class NotificationsModel extends BaseViewModel {
    static final boolean DEBUG = BuildConfig.DEBUG;
    static final String TAG = NotificationsModel.class.getSimpleName();

    MutableLiveData<StatusDataResource> mNotificationsStatus = new MutableLiveData<>();

    public MutableLiveData<StatusDataResource> getNotificationsStatus() {
        return mNotificationsStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAuthUser().accessToken).getService(GithubService.class);
    }

    public void loadNotifications(final NotificationType type, final int page, boolean isReload) {
        final boolean readCacheFirst = page == 1 && !isReload;

        HttpObserver<ArrayList<Notification>> httpObserver = new HttpObserver<ArrayList<Notification>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<Notification>> response) {
                LogUtils.d(TAG, "load notifications " + type + " success");
                ArrayList<NotificationData> notificationDataArrayList = convertFromNotification((ArrayList<Notification>) response.body());
                mNotificationsStatus.postValue(StatusDataResource.success(notificationDataArrayList, page));
            }

            @Override
            public void onError(Throwable error) {
                LogUtils.d(TAG, "load notifications error = " + error.getMessage());
                mNotificationsStatus.postValue(StatusDataResource.error(error.getMessage()));
            }
        };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<ArrayList<Notification>,
                Response<ArrayList<Notification>>>() {
            @Nullable
            @Override
            public Observable<Response<ArrayList<Notification>>> createObservable(boolean forceNetWork) {
                Observable<Response<ArrayList<Notification>>> responseObservable = null;
                switch (type) {
                    case Unread:
                        responseObservable = mGithubService.getNotifications(forceNetWork, false, false);
                        break;
                    case Participating:
                        responseObservable = mGithubService.getNotifications(forceNetWork, true, true);
                        break;
                    case All:
                        responseObservable = mGithubService.getNotifications(forceNetWork, true, false);
                        break;
                }
                return responseObservable;
            }
        }, httpObserver, readCacheFirst);
    }


    ArrayList<NotificationData> convertFromNotification(ArrayList<Notification> notifications) {
        LogUtils.d(TAG, "convert from notification, size = " + notifications.size());
        ArrayList<NotificationData> notificationDataArrayList = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationData notificationData = new NotificationData();
            notificationData.title = notification.subject.title;
            notificationData.time = BaseUtils.getNewsTimeStr(CoreManager.getContext(), notification.updateAt);
            notificationData.unread = notification.unread;
            notificationData.subjectType = notification.subject.type;
            notificationDataArrayList.add(notificationData);
            if (DEBUG)
                LogUtils.d(TAG, "convert from notification, source data = " + notification.toString());
            if (DEBUG)
                LogUtils.d(TAG, "convert from notification, target data = " + notificationData.toString());
        }

        return notificationDataArrayList;
    }
}
