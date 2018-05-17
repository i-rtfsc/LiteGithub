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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BaseUtils {

    public static final String SEPARATOR = ",";

    public static List<String> string2List(@NonNull String str) {
        List<String> list = null;
        if (!str.contains(SEPARATOR)) {
            return list;
        }
        String[] strs = str.split(SEPARATOR);
        list = Arrays.asList(strs);
        return list;
    }

    public static String list2String(@NonNull List<String> list) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (list.size() == 0) {
            return stringBuilder.toString();
        }
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
            if (i != list.size() - 1) {
                stringBuilder.append(SEPARATOR);
            }
        }
        return stringBuilder.toString();
    }

    public static boolean isBlank(@Nullable String str) {
        boolean b = true;
        if (isNull(str)) {
            return b;
        }

        str = str.trim(); // 去掉空格
        if (!str.equals("")) {
            b = false;
        }
        return b;
    }

    public static boolean isEmpty(CharSequence str) {
        return isNull(str) || str.length() == 0;
    }

    public static boolean isEmpty(Object[] os) {
        return isNull(os) || os.length == 0;
    }

    public static boolean isEmpty(Number[] os) {
        return isNull(os) || os.length == 0;
    }

    public static boolean isEmpty(Collection<?> l) {
        return isNull(l) || l.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> m) {
        return isNull(m) || m.isEmpty();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }
}
