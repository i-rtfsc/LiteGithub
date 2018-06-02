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

package com.journeyOS.core.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.journeyOS.base.Constant;
import com.journeyOS.base.network.NetWork;
import com.journeyOS.base.persistence.SpUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpCoreManager {
    private static final String TAG = HttpCoreManager.class.getSimpleName();

    public static <T, R extends Response<T>> void executeRxHttp(
            @NonNull IObservableCreator<T, R> observableCreator, @NonNull HttpObserver<T> httpObserver) {
        executeRxHttp(observableCreator, httpObserver, false);
    }

    public static <T, R extends Response<T>> void executeRxHttp(
            @NonNull final IObservableCreator<T, R> observableCreator, @NonNull final HttpObserver<T> httpObserver,
            final boolean readCacheFirst) {

        final HttpObserver<T> tempObserver = new HttpObserver<T>() {
            @Override
            public void onError(Throwable error) {
                httpObserver.onError(error);
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<T> response) {
                LogUtils.i(TAG, "get data ok:" + System.currentTimeMillis());
                LogUtils.i(TAG, "data:" + response.body());
                if (response.isSuccessful()) {
                    if (readCacheFirst && response.isFromCache()
                            && NetWork.isAvailable(CoreManager.getContext())) {
                        executeRxHttp(observableCreator.createObservable(true),
                                getHttpSubscriber(this));
                    }
                    httpObserver.onSuccess(response);
                } else {
                    httpObserver.onError(new HttpError(HttpErrorCode.NO_CACHE_AND_NETWORK));
                }

            }
        };
        boolean useCache = SpUtils.getInstant().getBoolean(Constant.THEME, true);
        executeRxHttp(observableCreator.createObservable(!readCacheFirst || !useCache),
                getHttpSubscriber(tempObserver));
        LogUtils.i(TAG, "get cache start:" + System.currentTimeMillis());
    }

    public static <T, R extends Response<T>> void executeRxHttp(
            @NonNull Observable<R> observable, @Nullable HttpSubscriber<T> subscriber) {
        if (subscriber != null) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpSubscriber<T>());
        }
    }

    public interface IObservableCreator<T, R extends Response<T>> {
        Observable<R> createObservable(boolean forceNetWork);
    }

    static <T> HttpSubscriber getHttpSubscriber(
            HttpObserver<T> httpObserver) {
        return new HttpSubscriber<>(httpObserver);
    }
}
