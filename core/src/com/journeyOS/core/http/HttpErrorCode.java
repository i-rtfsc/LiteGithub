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

package com.journeyOS.core.http;

import androidx.annotation.StringRes;

import com.journeyOS.core.CoreManager;
import com.journeyOS.core.R;

import java.util.HashMap;
import java.util.Map;

public class HttpErrorCode {
    /**
     * No cache and network available.
     */
    public final static int NO_CACHE_AND_NETWORK = 0;

    private final static Map<Integer, String> ERROR_MSG_MAP = new HashMap<>();

    static{
        ERROR_MSG_MAP.put(NO_CACHE_AND_NETWORK, getString(R.string.no_cache_and_network));
    }

    public static String getErrorMsg(int errorCode){
        return ERROR_MSG_MAP.get(errorCode);
    }

    private static String getString(@StringRes int resId){
        return CoreManager.getContext().getResources().getString(resId);
    }
}
