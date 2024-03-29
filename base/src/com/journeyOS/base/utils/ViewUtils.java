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

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.journeyOS.base.R;

public class ViewUtils {

    @ColorInt
    public static int getPrimaryColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimary);
    }

    @ColorInt
    public static int getPrimaryDarkColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimaryDark);
    }

    @ColorInt
    public static int getAccentColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorAccent);
    }

    @ColorInt
    public static int getPrimaryTextColor(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.textColorPrimary);
    }

    @ColorInt
    public static int getSecondaryTextColor(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.textColorSecondary);
    }

    @ColorInt
    public static int getWindowBackground(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.windowBackground);
    }

    @ColorInt
    private static int getColorAttr(@NonNull Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{attr});
        final int color = typedArray.getColor(0, Color.LTGRAY);
        typedArray.recycle();
        return color;
    }

    public static int[] getRefreshLayoutColors(Context context) {
        return new int[]{
                ViewUtils.getAccentColor(context),
                ViewUtils.getPrimaryColor(context),
                ViewUtils.getPrimaryDarkColor(context)
        };
    }
}
