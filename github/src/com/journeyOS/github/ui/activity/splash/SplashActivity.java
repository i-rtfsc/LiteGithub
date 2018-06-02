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

package com.journeyOS.github.ui.activity.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.userprovider.AuthUser;
import com.journeyOS.core.api.userprovider.IAuthUserProvider;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.activity.login.LoginActivity;
import com.journeyOS.github.ui.activity.main.GithubActivity;

public class SplashActivity extends BaseActivity {
    final String TAG = SplashActivity.class.getSimpleName();
    final int REQUEST_ACCESS_TOKEN = 1;
    Context mContext;

    public static void newInstance(@NonNull Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.splash_activity;
    }

    @Override
    public void initViews() {
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);

        CoreManager.getImpl(IAuthUserProvider.class).getUserWorkHandler().post(new Runnable() {
            @Override
            public void run() {
                AuthUser authUser = CoreManager.getImpl(IAuthUserProvider.class).getAuthUser();
                LogUtils.d(TAG, "user has been login = " + !BaseUtils.isNull(authUser));
                if (BaseUtils.isNull(authUser)) {
                    startActivityForResult(new Intent(mContext, LoginActivity.class), REQUEST_ACCESS_TOKEN);
                } else {
                    CoreManager.setAuthUser(authUser);
                    showMainPage();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCESS_TOKEN:
                if (resultCode == RESULT_OK) {
                    showMainPage();
                }
                break;
            default:
                break;
        }

    }

    void showMainPage() {
        delayFinish();
        startActivity(new Intent(mContext, GithubActivity.class));
    }
}
