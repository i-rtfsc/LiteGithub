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

import android.app.ProgressDialog;
import android.content.DialogInterface;

import static com.journeyOS.base.utils.ToastyUtils.ToastType;

public interface IBaseView {

    void showProgressDialog(String content);

    void dismissProgressDialog();

    ProgressDialog getProgressDialog(String content);

    void showToast(ToastType toastType, String message, boolean isLong);

    void showToast(ToastType toastType, int resourceId, boolean isLong);

    void showTipDialog(String content);

    void showConfirmDialog(String msn, String title, String confirmText, DialogInterface.OnClickListener confirmListener);
}
