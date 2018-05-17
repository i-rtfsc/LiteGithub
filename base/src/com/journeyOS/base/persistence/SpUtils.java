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
 */package com.journeyOS.base.persistence;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;

import java.util.Set;

public class SpUtils {
    private static final String TAG = SpUtils.class.getSimpleName();
    private static final String PRE_NAME = "configs";
    private static Context mContext;
    private static SpUtils instant;
    private static SharedPreferences sp;

    private SpUtils(Context context) {
        this.mContext = context;
        sp = mContext.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
    }

    public static SpUtils init(Context context) {
        if (instant == null) {
            instant = new SpUtils(context);
        }
        return instant;
    }

    public static SpUtils getInstant() {
        if (instant == null) {
            LogUtils.d(TAG, "please init first...");
            //instant = new SpUtils(mContext);
        }
        sp = mContext.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        return instant;
    }

    public static SpUtils getInstant(String prefName) {
        if (instant == null) {
            LogUtils.d(TAG, "please init first...");
            //instant = new SpUtils(mContext);
        }
        if (BaseUtils.isNull(prefName)) {
            sp = mContext.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        } else {
            sp = mContext.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        }
        return instant;
    }

    public void put(@NonNull final String key, final boolean value) {
        put(key, value, false);
    }

    public void put(@NonNull final String key, final boolean value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putBoolean(key, value).commit();
        } else {
            sp.edit().putBoolean(key, value).apply();
        }
    }

    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, true);
    }

    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }


    public void put(@NonNull final String key, final int value) {
        put(key, value, false);
    }

    public void put(@NonNull final String key, final int value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putInt(key, value).commit();
        } else {
            sp.edit().putInt(key, value).apply();
        }
    }

    public int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }

    public int getInt(@NonNull final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }


    public void put(@NonNull final String key, @NonNull final String value) {
        // 从效率上推荐使用apply
        put(key, value, false);
    }

    public void put(@NonNull final String key, @NonNull final String value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putString(key, value).commit();
        } else {
            sp.edit().putString(key, value).apply();
        }
    }

    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    public String getString(@NonNull final String key, @NonNull final String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void put(@NonNull final String key, @NonNull final Set<String> value) {
        // 从效率上推荐使用apply
        put(key, value, false);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void put(@NonNull final String key, @NonNull final Set<String> value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putStringSet(key, value).commit();
        } else {
            sp.edit().putStringSet(key, value).apply();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(@NonNull final String key) {
        return getStringSet(key, null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(@NonNull final String key, @NonNull final Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    public void clear() {
        clear(false);
    }

    public void clear(final boolean isCommit) {
        if (isCommit) {
            sp.edit().clear().commit();
        } else {
            sp.edit().clear().apply();
        }
    }

    public void remove(@NonNull String key) {
        sp.edit().remove(key);
    }
}
