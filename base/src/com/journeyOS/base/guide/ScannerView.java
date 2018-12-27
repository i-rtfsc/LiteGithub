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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ScannerView extends View {
    //备用Paint
    private Paint sPaint;
    //用于存储上次位置信息
    private RectF lastRegion = new RectF();
    //用于存储目标位置信息
    private RectF sRegion = new RectF();
    //当前View的位置信息
    private RectF layoutRegion = new RectF();
    //当前扫描目标的索引，备用
    private int scanIndex = 0;

    public ScannerView(Context context, float sLeft, float sTop, float sBottom, float sRight) {
        super(context);
        sRegion.left = sLeft;
        sRegion.top = sTop;
        sRegion.bottom = sBottom;
        sRegion.right = sRight;
        init();
    }

    public ScannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        initPaint();
    }

    //备用
    private void initPaint() {
        sPaint = new Paint();
        sPaint.setStyle(Paint.Style.STROKE);
        sPaint.setColor(Color.RED);
        sPaint.setStrokeWidth(3);
    }

    public void setScannerRegion(float left, float top, float bottom, float right) {
        //上个目标
        lastRegion.set(sRegion.centerX(), sRegion.centerY(), sRegion.centerX(), sRegion.centerY());

        //目标区域
        sRegion.left = left;
        sRegion.top = top;
        sRegion.bottom = bottom;
        sRegion.right = right;

        //框区域
        layoutRegion.top = sRegion.centerY();
        layoutRegion.bottom = sRegion.centerY();
        layoutRegion.left = sRegion.centerX();
        layoutRegion.right = sRegion.centerX();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams.width = 0;
        layoutParams.height = 0;
        setLayoutParams(layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setScannerRegion(RectF region) {
        setScannerRegion(region.left, region.top, region.bottom, region.right);
    }

    public void reset() {
        sRegion.left = 0;
        sRegion.right = 0;
        sRegion.top = 0;
        sRegion.bottom = 0;
    }

    public Paint getsPaint() {
        return sPaint;
    }

    //备用
    public void setsPaint(Paint sPaint) {
        this.sPaint = sPaint;
    }

    public int getScanIndex() {
        return scanIndex;
    }

    public void setScanIndex(int scanIndex) {
        this.scanIndex = scanIndex;
    }

    public RectF getsRegion() {
        return sRegion;
    }

    public RectF getLayoutRegion() {
        return layoutRegion;
    }

    /**
     * 因为View没有自带的set方法可以直接一起设置View的上下左右位置，所以这里封装Layout方法来实现这个效果。
     *
     * @param lr View的Layout目标区域
     */
    public void setLayoutRegion(RectF lr) {
        layoutRegion.set(lr);
        layout((int) lr.left, (int) lr.top, (int) lr.right, (int) lr.bottom);
    }

    public RectF getLastRegion() {
        return lastRegion;
    }

}
