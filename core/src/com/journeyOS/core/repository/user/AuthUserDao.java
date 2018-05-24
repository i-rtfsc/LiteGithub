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

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.journeyOS.core.api.userprovider.AuthUser;

import java.util.List;

@Dao
public interface AuthUserDao {

    @Query("SELECT * FROM authUser")
    List<AuthUser> getAuthUsers();

    @Query("SELECT * FROM authUser LIMIT 1")
    AuthUser getAuthUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AuthUser> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AuthUser user);

    @Delete
    void delete(AuthUser user);

    @Query("DELETE FROM authUser")
    void deleteAll();
}
