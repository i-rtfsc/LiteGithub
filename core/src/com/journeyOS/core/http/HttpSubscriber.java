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

import retrofit2.Response;
import rx.Subscriber;

public class HttpSubscriber<T, R extends Response<T>> extends Subscriber<R> {
    private HttpObserver<T> mObserver;

    public HttpSubscriber() {
    }

    public HttpSubscriber(HttpObserver<T> observer) {
        mObserver = observer;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (mObserver != null) {
            mObserver.onError(e);
        }
    }

    @Override
    public void onNext(R r) {
        if (mObserver != null) {
            mObserver.onSuccess(new HttpResponse<>(r));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}