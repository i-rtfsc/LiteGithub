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

package com.journeyOS.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.journeyOS.base.R;

public class SettingText extends SettingView {
    private TextView mText;

    private String mTextContent;
    private int mTextSize;
    private int mTextColor;

    public SettingText(Context context) {
        this(context, null);
    }

    public SettingText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDefaultValue();

        init(attrs);
    }

    private void setDefaultValue() {
        mTextContent = "";
        mTextSize = 0;
        mTextColor = DEFAULT_COLOR;
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SettingText);
        if (ta != null) {
            mTextContent = ta.getString(R.styleable.SettingText_settingText);
            mTextSize = ta.getDimensionPixelSize(R.styleable.SettingText_settingTextSize, 0);
            mTextColor = ta.getColor(R.styleable.SettingText_settingTextColor, DEFAULT_COLOR);
        }

        if (mTextSize == 0) {
            mTextSize = 14;
        } else {
            mTextSize = px2dp(mTextSize);
        }

        mText.setText(mTextContent);
        mText.setTextSize(mTextSize);
        mText.setTextColor(mTextColor);

        ta.recycle();
    }

    @Override
    protected View getRightView() {
        mText = new TextView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mText.setLayoutParams(params);
        return mText;
    }

    public void setRightTextColor(int color) {
        mText.setTextColor(color);
    }

    public void setRightTextSize(int size) {
        mText.setTextSize(size);
    }

    public void setRightText(String content) {
        hideHelpView();
        mText.setText(content);
    }

    public String getRightText() {
        return mText.getText().toString();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }
}
