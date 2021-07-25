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

package com.journeyOS.core.repository;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.annotation.MainThread;

import com.journeyOS.core.CoreManager;

public class DBHelper {

    @MainThread
    public static <T extends RoomDatabase> T provider(Class<T> dbCls, String dbName) {
         /*
          * 使用fallbackToDestructiveMigration暴力升级数据库，
          * 更多升级方式 see{@link # http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/0728/8278.html}
         */
        return Room.databaseBuilder(CoreManager.getContext(),
                dbCls, dbName).fallbackToDestructiveMigration().build();
    }
}
