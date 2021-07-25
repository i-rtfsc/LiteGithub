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

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.journeyOS.base.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

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

    public static String getDateStr(@NonNull Date date) {
        String regex;
        regex = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(regex);
        return format.format(date);
    }

    public static String getNewsTimeStr(@NonNull Context context, @NonNull Date date) {
        long subTime = System.currentTimeMillis() - date.getTime();
        final double MILLIS_LIMIT = 1000.0f;
        final double SECONDS_LIMIT = 60 * MILLIS_LIMIT;
        final double MINUTES_LIMIT = 60 * SECONDS_LIMIT;
        final double HOURS_LIMIT = 24 * MINUTES_LIMIT;
        final double DAYS_LIMIT = 30 * HOURS_LIMIT;
        if (subTime < MILLIS_LIMIT) {
            return context.getString(R.string.just_now);
        } else if (subTime < SECONDS_LIMIT) {
            return Math.round(subTime / MILLIS_LIMIT) + " " + context.getString(R.string.seconds_ago);
        } else if (subTime < MINUTES_LIMIT) {
            return Math.round(subTime / SECONDS_LIMIT) + " " + context.getString(R.string.minutes_ago);
        } else if (subTime < HOURS_LIMIT) {
            return Math.round(subTime / MINUTES_LIMIT) + " " + context.getString(R.string.hours_ago);
        } else if (subTime < DAYS_LIMIT) {
            return Math.round(subTime / HOURS_LIMIT) + " " + context.getString(R.string.days_ago);
        } else
            return getDateStr(date);
    }

    public static String getSizeString(int size) {
        if (size < 1024) {
            return String.format(Locale.getDefault(), "%d B", size);
        } else if (size < 1024 * 1024) {
            float sizeK = size / 1024f;
            return String.format(Locale.getDefault(), "%.2f KB", sizeK);
        } else if (size < 1024 * 1024 * 1024) {
            float sizeM = size / (1024f * 1024f);
            return String.format(Locale.getDefault(), "%.2f MB", sizeM);
        }
        return null;
    }

    public static void copyToClipboard(@NonNull Context context, @NonNull String uri) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getString(R.string.app_name), uri);
        clipboard.setPrimaryClip(clip);
        Toasty.success(context, context.getString(R.string.success_copied)).show();
    }

    public static void openInBrowser(@NonNull Context context, @NonNull String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void launchEmail(@NonNull Context context, @NonNull String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (ActivityNotFoundException e) {
            Toasty.warning(context, context.getString(R.string.no_email_clients)).show();
        }
    }

    public static void openInMarket(@NonNull Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.open_in_market))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (ActivityNotFoundException e) {
            Toasty.warning(context, context.getString(R.string.no_market_clients)).show();
        }
    }

    public static void shareText(@NonNull Context context, @NonNull String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        try {
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_to)));
        } catch (ActivityNotFoundException e) {
            Toasty.warning(context, context.getString(R.string.no_share_clients)).show();
        }
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
