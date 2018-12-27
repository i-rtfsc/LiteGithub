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

package com.journeyOS.base.guide;

import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

public class ScanTarget {
    //目标View
    private View mTargetView;

    //目标区域
    private RectF mRegion;

    //说明文字、跳过/忽略帮助的按钮的文字、下一步按钮的文字
    private String mShowText;
    private String mJumpText;
    private String mNextText;

    //弹出窗口的宽高
    private int mWindowWidth = 350;
    private int mWindowHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    //弹处窗口的XY偏移量，初始位置是在搜索框的正中下方
    private int wOffsetX = 0;
    private int wOffsetY = 0;

    //判断是否区域
    private boolean mIsRegion = false;

    public ScanTarget(View targetView, String text, int wOffsetX, int wOffsetY) {
        mShowText = text;
        mTargetView = targetView;
        this.wOffsetX = wOffsetX;
        this.wOffsetY = wOffsetY;
        init();
    }

    public ScanTarget(RectF region, String text, int wOffsetX, int wOffsetY) {
        mShowText = text;
        mRegion = region;
        this.wOffsetX = wOffsetX;
        this.wOffsetY = wOffsetY;
        init();
    }

    private void init() {
        if (mRegion != null) {
            mIsRegion = true;
        }
    }

    public RectF viewToRegion(int offsetX, int offsetY) {
        if (!mIsRegion) {
            RectF rectF = getViewLocationRectF(mTargetView);
            rectF.offset(offsetX, offsetY);
            setRegion(rectF);
        }
        return mRegion;
    }

    //获取View的位置矩阵，相对于Window的坐标系
    private RectF getViewLocationRectF(View view) {
        int[] location = {0, 0};
        view.getLocationInWindow(location);
        return new RectF(
                location[0]
                , location[1]
                , location[0] + view.getWidth()
                , location[1] + view.getHeight());
    }

    public int getWindowWidth() {
        return mWindowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.mWindowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return mWindowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.mWindowHeight = windowHeight;
    }

    public String getJumpText() {
        return mJumpText;
    }

    public void setJumpText(String jumpText) {
        this.mJumpText = jumpText;
    }

    public String getNextText() {
        return mNextText;
    }

    public void setNextText(String nextText) {
        this.mNextText = nextText;
    }

    public int getwOffsetX() {
        return wOffsetX;
    }

    public String getShowText() {
        return mShowText;
    }

    public int getwOffsetY() {
        return wOffsetY;
    }

    public RectF getRegion() {
        return mRegion;
    }

    public void setRegion(RectF mRegion) {
        this.mRegion = mRegion;
        init();
    }

    public View getTargetView() {
        return mTargetView;
    }

    public void setTargetView(View mTargetView) {
        this.mTargetView = mTargetView;
        init();
    }

    public boolean getIsRegion() {
        return mIsRegion;
    }
}
