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
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.journeyOS.base.R;

public class GuidePopupWindow extends PopupWindow {
    private Context mContext;
    private TyperTextView mTyperTextView;
    private TextView tvJump;
    private TextView tvNext;
    private OnWindowClickListener mOnWindowClickListener;
    private @LayoutRes
    int mContentId = R.layout.guide_tips_window;
    private int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    public GuidePopupWindow(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        View contentView = LayoutInflater.from(mContext).inflate(mContentId, null);
        setContentView(contentView);
        resetWidthAndHeight();

        mTyperTextView = (TyperTextView) contentView.findViewById(R.id.ttv_tips);
        if (mTyperTextView != null) {
            mTyperTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTyperTextView != null) {
                        mTyperTextView.showAll();
                    }
                }
            });
        }
        tvJump = (TextView) contentView.findViewById(R.id.tv_jump);
        if (tvJump != null) {
            tvJump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnWindowClickListener != null) {
                        mOnWindowClickListener.onJumpClick();
                    }
                }
            });
//            tvJump.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
        tvNext = (TextView) contentView.findViewById(R.id.tv_next);
        if (tvNext != null) {
            tvNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnWindowClickListener != null) {
                        mOnWindowClickListener.onNextClick();
                    }
                }
            });
//            tvNext.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }


    }

    public void setContent(@LayoutRes int layoutId) {
        mContentId = layoutId;
        init();
    }

    public void resetWidthAndHeight() {
        setWidth(mWidth);
        setHeight(mHeight);
    }


    public void setContentBackgroundId(@DrawableRes int id) {
        setBackgroundDrawable(mContext.getResources().getDrawable(id));
    }

    public void setJumpText(String text) {
        if (tvJump != null) {
            tvJump.setText(text);
        }
    }

    public void setNextText(String text) {
        if (tvNext != null) {
            tvNext.setText(text);
        }
    }

    public void setTvSize(int size) {
        if (tvJump != null && tvNext != null) {
            tvJump.setTextSize(size);
            tvNext.setTextSize(size);
        }
    }

    public void setTyperIncrease(int increase) {
        if (mTyperTextView != null) {
            mTyperTextView.setCharIncrease(increase);
        }
    }

    public void setTyperTextSize(int size) {
        if (mTyperTextView != null) {
            mTyperTextView.setTextSize(size);
        }
    }

    public void setTyperRefreshTime(int refreshTime) {
        if (mTyperTextView != null) {
            mTyperTextView.setTyperRefreshTime(refreshTime);
        }
    }

    public void showGuideText(String text) {
        if (mTyperTextView != null) {
            mTyperTextView.animateText(text);
        }
    }


    public void setOnWindowClickListener(OnWindowClickListener mOnWindowClickListener) {
        this.mOnWindowClickListener = mOnWindowClickListener;
    }

    interface OnWindowClickListener {
        void onNextClick();

        void onJumpClick();
    }
}
