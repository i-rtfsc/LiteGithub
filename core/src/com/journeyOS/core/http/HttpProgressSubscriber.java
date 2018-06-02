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

import android.app.AlertDialog;
import android.support.annotation.NonNull;

import com.journeyOS.base.utils.BaseUtils;

import retrofit2.Response;

public class HttpProgressSubscriber<T, R extends Response<T>> extends HttpSubscriber<T> {
    private AlertDialog mDialog;

    public HttpProgressSubscriber(@NonNull AlertDialog dialog, @NonNull HttpObserver<T> observer) {
        super(observer);
        mDialog = dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isUnsubscribed() && !BaseUtils.isNull(mDialog)) {
            mDialog.show();
        }
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        if (!BaseUtils.isNull(mDialog)) mDialog.dismiss();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (!BaseUtils.isNull(mDialog)) mDialog.dismiss();
    }
}
