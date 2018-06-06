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

package com.journeyOS.base.utils;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class ToastyUtils {
    public enum ToastType {
        NORMAL, INFO, WARNING, ERROR, SUCCESS
    }

    public static void showToast(Context context, ToastType toastType, String message, boolean isLong) {
        switch (toastType) {
            case NORMAL:
                Toasty.normal(context, message, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                break;
            case INFO:
                Toasty.info(context, message, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                break;
            case WARNING:
                Toasty.warning(context, message, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                break;
            case ERROR:
                Toasty.error(context, message, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                break;
            case SUCCESS:
                Toasty.success(context, message, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static void showToast(Context context, ToastType toastType, int resourceId, boolean isLong) {
        switch (toastType) {
            case NORMAL:
                Toasty.normal(context, context.getString(resourceId), isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                break;
            case INFO:
                Toasty.info(context, context.getString(resourceId), isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                break;
            case WARNING:
                Toasty.warning(context, context.getString(resourceId), isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                break;
            case ERROR:
                Toasty.error(context, context.getString(resourceId), isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                break;
            case SUCCESS:
                Toasty.success(context, context.getString(resourceId), isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
