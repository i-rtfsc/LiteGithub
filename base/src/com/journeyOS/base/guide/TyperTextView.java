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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.journeyOS.base.R;

import java.util.Random;

@SuppressLint("AppCompatCustomView")
public class TyperTextView extends TextView {
    public static final String TAG = TyperTextView.class.getSimpleName();
    public static final int SHOWING = 1;
    public static final int SHOW_ALL = 2;
    private Random random;
    private CharSequence mText;
    private ShowHandler handler;
    private int charIncrease;
    private int showingIncrease;
    private int typerRefreshTime;
    private AnimationListener animationListener;

    public TyperTextView(Context context) {
        this(context, null);
    }

    public TyperTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TyperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TyperTextView);
        typerRefreshTime = typedArray.getInt(R.styleable.TyperTextView_typerRefreshTime, 100);
        charIncrease = typedArray.getInt(R.styleable.TyperTextView_charIncrease, 1);
        typedArray.recycle();

        //使用随机数，增加飘忽感
        random = new Random();
        mText = getText();
        handler = new ShowHandler();
    }

    public void setAnimationListener(AnimationListener listener) {
        animationListener = listener;
    }


    public int getTyperRefreshTime() {
        return typerRefreshTime;
    }

    public void setTyperRefreshTime(int typerRefreshTime) {
        this.typerRefreshTime = typerRefreshTime;
    }

    public int getCharIncrease() {
        return charIncrease;
    }

    public void setCharIncrease(int charIncrease) {
        this.charIncrease = charIncrease;
    }

    public void setProgress(float progress) {
        setText(mText.subSequence(0, (int) (mText.length() * progress)));
    }

    public void animateText(CharSequence text) {
        if (text == null) {
            throw new RuntimeException("text must not be null");
        }

        mText = text;
        showingIncrease = charIncrease;
        setText("");
        Message message = Message.obtain();
        message.what = SHOWING;
        handler.sendMessage(message);
    }

    public void showAll() {
        handler.removeMessages(SHOWING);
        Message message = Message.obtain();
        message.what = SHOW_ALL;
        handler.sendMessage(message);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    public interface AnimationListener {
        void onAnimationProceeding(TyperTextView typerTextView);

        void onAnimationEnd(TyperTextView typerTextView);
    }

    private class ShowHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int currentLength = getText().length();
            if (currentLength + showingIncrease > mText.length()) {
                showingIncrease = mText.length() - currentLength;
            }
            switch (msg.what) {
                case SHOWING:
                    if (currentLength < mText.length()) {
                        append(mText.subSequence(currentLength, currentLength + showingIncrease));
                        long randomTime = typerRefreshTime + random.nextInt(typerRefreshTime);
                        Message message = Message.obtain();
                        message.what = SHOWING;
                        handler.sendMessageDelayed(message, randomTime);
                    } else {
                        if (animationListener != null) {
                            animationListener.onAnimationEnd(TyperTextView.this);
                        }
                    }
                    break;
                case SHOW_ALL:
                    setText(mText);
                    break;
            }
        }
    }
}
