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

import android.os.Build;
import android.util.Log;

public class LogUtils {
    public static final String TAG = "LiteGitHub";
    //add user type for more debug log with beta user...
    public final static boolean BUILD_TYPE = "eng".equals(Build.TYPE) || "userdebug".equals(Build.TYPE) || "user".equals(Build.TYPE);
    public static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG) || BUILD_TYPE;

    public static void v(String message, Object... args) {
        if (DEBUG) {
            Log.v(TAG, args == null || args.length == 0
                    ? message : String.format(message, args));
        }
    }

    public static void v(String tag, String message, Object... args) {
        if (DEBUG) {
            Log.v(replaceTag(tag), args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void d(String message, Object... args) {
        if (DEBUG) {
            Log.d(TAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void d(String tag, String message, Object... args) {
        if (DEBUG) {
            Log.d(replaceTag(tag), args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void d(String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String message, Object... args) {
        if (DEBUG) {
            Log.i(TAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void i(String tag, String message, Object... args) {
        if (DEBUG) {
            Log.i(replaceTag(tag), args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void w(String message, Object... args) {
        if (DEBUG) {
            Log.w(TAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void w(String tag, String message, Object... args) {
        if (DEBUG) {
            Log.w(replaceTag(tag), args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void e(String message, Object... args) {
        if (DEBUG) {
            Log.e(TAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void e(String tag, String message, Object... args) {
        if (DEBUG) {
            Log.e(replaceTag(tag), args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void e(String message, Exception e) {
        if (DEBUG) {
            Log.e(TAG, message, e);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if (DEBUG) {
            Log.e(replaceTag(tag), message, e);
        }
    }

    public static void wtf(String message, Object... args) {
        if (DEBUG) {
            Log.wtf(TAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void wtf(String tag, String message, Object... args) {
        if (DEBUG) {
            Log.wtf(replaceTag(tag), args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void trace(String tag, String message) {
        if (DEBUG) {
            Log.i(replaceTag(tag), message + "----------" + "\n" + Log.getStackTraceString(new Throwable()));
        }
    }

    private static String replaceTag(String src) {
        if (src == null) return null;

        String dest = null;
        if (src.contains(TAG)) {
            dest = TAG + "-" + src.substring(TAG.length(), src.length());
        } else {
            dest = TAG + "-" + src;
        }
        return dest;
    }
}