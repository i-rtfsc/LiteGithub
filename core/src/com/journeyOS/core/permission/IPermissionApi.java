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

package com.journeyOS.core.permission;


import android.support.annotation.NonNull;

import com.journeyOS.core.api.ICoreApi;
import com.journeyOS.core.base.BaseActivity;

public interface IPermissionApi extends ICoreApi {
    void initUrgentPermission(BaseActivity activity);

    void onRequestPermissionsResult(BaseActivity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
