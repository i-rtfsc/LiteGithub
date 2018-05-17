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

package com.journeyOS.core.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class StatusDataResource<T> {
    public enum Status {
        SUCCESS, ERROR, LOADING, CACHE_HIT
    }

    @NonNull
    public final Status status;
    @Nullable
    public T data;
    @Nullable
    public final String message;

    private StatusDataResource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> StatusDataResource<T> loading() {
        return new StatusDataResource<>(Status.LOADING, null, null);
    }

    public static <T> StatusDataResource<T> success(@NonNull T data) {
        return new StatusDataResource<>(Status.SUCCESS, data, null);
    }

    public static <T> StatusDataResource<T> error(String msg) {
        return new StatusDataResource<>(Status.ERROR, null, msg);
    }

    public static <T> StatusDataResource<T> cacheHit(@NonNull T data) {
        return new StatusDataResource<>(Status.CACHE_HIT, data, null);
    }

    public boolean isSucceed() {
        return Status.SUCCESS.equals(status);
    }
}
