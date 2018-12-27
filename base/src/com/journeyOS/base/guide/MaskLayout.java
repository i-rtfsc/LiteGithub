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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.journeyOS.base.R;

import java.util.ArrayList;

public class MaskLayout extends RelativeLayout implements View.OnClickListener, GuidePopupWindow.OnWindowClickListener {
    public static final String TAG = "guiderview";
    private Context mContext;
    private LiteGuide mLiteGuide;
    private Paint sPaint;
    private ArrayList<ScannerView> mScannerList;
    private ArrayList<ScanTarget> mScanTargets;
    private boolean isDoingAnimation = false;
    private int scanIndex = 0;
    private OnGuideClickListener mClickListener;
    private GuidePopupWindow mGuidePopupWindow;
    private int mRefreshTime = 20;
    private int mMoveDuration = 500;
    private int mExpandDuration = 500;
    private @ColorInt
    int mMaskColor;
    private AnimatorSet mDoAnimator;

    public MaskLayout(Context context, LiteGuide liteGuide) {
        super(context);
        mLiteGuide = liteGuide;
        init(context);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: ");
        mLiteGuide.checkContentLocation();
    }

    private void init(Context context) {
        mContext = context;
        checkAPILevel();
        setOnClickListener(this);
        setWillNotDraw(false);

        mMaskColor = mContext.getResources().getColor(android.R.color.darker_gray);

        initPaint();
        initScanner();
        mGuidePopupWindow = new GuidePopupWindow(mContext);
        mGuidePopupWindow.setContentBackgroundId(R.drawable.guide_button_shape);
        mGuidePopupWindow.setOnWindowClickListener(this);

        if (mClickListener != null) {
            mClickListener.onGuideStart();
        }

    }

    //API小于18则关闭硬件加速，否则clipRect()方法不生效
    private void checkAPILevel() {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(LAYER_TYPE_NONE, null);
        }
        setLayerType(LAYER_TYPE_NONE, null);
    }

    private void initPaint() {
        sPaint = new Paint();
        sPaint.setStyle(Paint.Style.STROKE);
        sPaint.setColor(Color.RED);
        sPaint.setStrokeWidth(3);
    }

    private void initScanner() {
        mScannerList = new ArrayList<>();
        mScanTargets = new ArrayList<>();

        ScannerView scannerView = new ScannerView(mContext, 0, 0, 0, 0);
        scannerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onTarget(scanIndex - 1);
                }
            }
        });
        addView(scannerView);
        mScannerList.add(scannerView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < getChildCount(); i++) {
            ScannerView child = (ScannerView) getChildAt(i);

            if (!isDoingAnimation) {
                clipHighlight(canvas, child.getsRegion());
            } else {
                canvas.drawColor(mMaskColor);
            }
            drawScannerLine(canvas, child);
        }

        if (isDoingAnimation) {
            postInvalidateDelayed(mRefreshTime);
        }
    }

    //剪切出高亮部分，给其余部分上色
    private void clipHighlight(Canvas canvas, RectF rectF) {
        canvas.save();
        canvas.clipRect(rectF, Region.Op.DIFFERENCE);
        canvas.drawColor(mMaskColor);
        canvas.restore();
    }

    private void drawScannerLine(Canvas canvas, ScannerView view) {
        float y = view.getY() + view.getHeight() / 2;
        float x = view.getX() + view.getWidth() / 2;

//        canvas.drawRect(view.getLeft(),view.getTop(),view.getRight(),view.getBottom(),view.getsPaint());
        canvas.drawRect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom(), sPaint);
        canvas.drawLine(0, y, view.getLeft(), y, sPaint);
        canvas.drawLine(view.getRight(), y, canvas.getWidth(), y, sPaint);
        canvas.drawLine(x, 0, x, view.getTop(), sPaint);
        canvas.drawLine(x, view.getBottom(), x, canvas.getHeight(), sPaint);
    }

    //Mask被点击
    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.onMask();
        }
    }

    //下一步
    public void onNext() {
        if (scanIndex < mScanTargets.size()) {
            //如果不是最后一个Target
            if ((mClickListener != null)) {
                mClickListener.onGuideNext(scanIndex);
            }
            animate(mScannerList.get(0)
                    , mScanTargets.get(scanIndex));
        } else {
            exit();
        }

    }

    private RectF getCenterRectF(RectF input) {
        return new RectF(input.centerX(), input.centerY(), input.centerX(), input.centerY());
    }

    //设置属性动画，主要是将ScannerView从上一个地方移动到下一个目标然后放大
    private void animate(final ScannerView scannerView, ScanTarget target) {
        if (target.getIsRegion()) {
            mScannerList.get(0).setScannerRegion(target.getRegion());
        } else {
            mScannerList.get(0).setScannerRegion(target.viewToRegion(-mLiteGuide.getContentLocationX(), -mLiteGuide.getContentLocationY()));
        }

        //设置跳过和下一步的字符
        if (target.getJumpText() == null) {
            target.setJumpText(mLiteGuide.getJumpText());
        }
        if (target.getNextText() == null) {
            target.setNextText(mLiteGuide.getNextText());
        }

        ObjectAnimator moveAnimator = ObjectAnimator.ofObject(scannerView, "layoutRegion", new RegionEvaluator(), scannerView.getLastRegion(), getCenterRectF(target.getRegion()));
        ObjectAnimator expandAnimator = ObjectAnimator.ofObject(scannerView, "layoutRegion", new RegionEvaluator(), scannerView.getLayoutRegion(), scannerView.getsRegion());

        moveAnimator.setDuration(mMoveDuration);
        expandAnimator.setDuration(mExpandDuration);

        mDoAnimator = new AnimatorSet();
        mDoAnimator.play(expandAnimator).after(moveAnimator);

        mDoAnimator.addListener(new AbstractAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mGuidePopupWindow.dismiss();
                setClickable(false);
                //启动不断draw来刷新扫描框移动的画面
                isDoingAnimation = true;
                postInvalidate();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isDoingAnimation = false;
                setClickable(true);
                showWindow();
                scanIndex++;
            }
        });

        mDoAnimator.start();
    }

    private void showWindow() {
        ScanTarget scanTarget = mScanTargets.get(scanIndex);
        mGuidePopupWindow.setWidth(scanTarget.getWindowWidth());
        mGuidePopupWindow.setHeight(scanTarget.getWindowHeight());
        mGuidePopupWindow.setNextText(scanTarget.getNextText());
        mGuidePopupWindow.setJumpText(scanTarget.getJumpText());
        mGuidePopupWindow.showAsDropDown(
                mScannerList.get(0)
                , mScanTargets.get(scanIndex).getwOffsetX()
                , mScanTargets.get(scanIndex).getwOffsetY());
        mGuidePopupWindow.showGuideText(scanTarget.getShowText());
    }

    public int getRefreshTime() {
        return mRefreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.mRefreshTime = refreshTime;
    }

    public void setsPaint(Paint sPaint) {
        this.sPaint = sPaint;
    }

    public void setMackColor(@ColorInt int color) {
        mMaskColor = color;
    }

    public void setScanTargets(ArrayList<ScanTarget> scanTargets) {
        mScanTargets = scanTargets;
    }

    public void setOnGuiderClickListener(OnGuideClickListener onGuiderClickListener) {
        this.mClickListener = onGuiderClickListener;
    }

    public void setMoveDuration(int moveDuration) {
        this.mMoveDuration = moveDuration;
    }

    public void setExpandDuration(int expandDuration) {
        this.mExpandDuration = expandDuration;
    }

    public GuidePopupWindow getWindow() {
        return mGuidePopupWindow;
    }

    @Override
    public void onNextClick() {
        if (mClickListener != null) {
            mClickListener.onNext(scanIndex);
        }
        onNext();
    }

    //退出时候做的操作
    public void exit() {
        if (mClickListener != null) {
            mClickListener.onGuideFinished();
        }
        if (mDoAnimator != null) {
            mDoAnimator.cancel();
        }
        reset();
        mLiteGuide.setIsGuiding(false);
        ViewGroup parent = (ViewGroup) getParent();
        parent.removeView(this);
        mGuidePopupWindow.dismiss();
    }

    private void reset() {
        scanIndex = 0;
        mScannerList.get(0).reset();
    }

    @Override
    public void onJumpClick() {
        if (mClickListener != null) {
            mClickListener.onJump();
        }
        exit();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onNext();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mGuidePopupWindow.dismiss();
    }

    public abstract class AbstractAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
