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

package com.journeyOS.github.ui.activity.main;

import androidx.lifecycle.MutableLiveData;

import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.thread.ICoreExecutorsApi;
import com.journeyOS.core.api.userprovider.AuthUser;
import com.journeyOS.core.api.userprovider.IAuthUserProvider;
import com.journeyOS.core.viewmodel.BaseViewModel;

public class GithubModel extends BaseViewModel {

    MutableLiveData<AuthUser> mAuthUser = new MutableLiveData<>();

    protected MutableLiveData<AuthUser> getAuthUserStatus() {
        return mAuthUser;
    }

    @Override
    protected void onCreate() {

    }

    protected void searchAuthUser() {
        CoreManager.getImpl(ICoreExecutorsApi.class).diskIOThread().execute(new Runnable() {
            @Override
            public void run() {
                AuthUser authUser = CoreManager.getImpl(IAuthUserProvider.class).getAuthUser();
                mAuthUser.postValue(authUser);
            }
        });
    }

}
