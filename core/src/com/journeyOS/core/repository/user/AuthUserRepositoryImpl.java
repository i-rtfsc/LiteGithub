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

package com.journeyOS.core.repository.user;

import com.journeyOS.core.api.userprovider.AuthUser;
import com.journeyOS.core.api.userprovider.IAuthUserProvider;
import com.journeyOS.core.repository.DBHelper;
import com.journeyOS.literouter.annotation.ARouterInject;

import java.util.List;

@ARouterInject(api = IAuthUserProvider.class)
public class AuthUserRepositoryImpl implements IAuthUserProvider {
    private static final String TAG = AuthUserRepositoryImpl.class.getSimpleName();
    private AuthUserDatabase mAuthUserDatabase;

    @Override
    public void onCreate() {
        mAuthUserDatabase = DBHelper.provider(AuthUserDatabase.class, "user.db");
    }

    @Override
    public List<AuthUser> getAuthUsers() {
        return mAuthUserDatabase.authUserDao().getAuthUsers();
    }


    @Override
    public AuthUser getAuthUser() {
        return mAuthUserDatabase.authUserDao().getAuthUser();
    }

    @Override
    public void insertOrUpdateAuthUser(AuthUser user) {
        mAuthUserDatabase.authUserDao().insert(user);
    }

    @Override
    public void deleteAuthUser(AuthUser user) {
        mAuthUserDatabase.authUserDao().delete(user);
    }

    @Override
    public void deleteAll() {
        mAuthUserDatabase.authUserDao().deleteAll();
    }

}
