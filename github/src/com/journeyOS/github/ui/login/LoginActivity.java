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

package com.journeyOS.github.ui.login;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.BasicToken;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.user_name_et)
    TextInputEditText userNameEt;
    @BindView(R.id.user_name_layout)
    TextInputLayout userNameLayout;
    @BindView(R.id.password_et)
    TextInputEditText passwordEt;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.login_bn)
    SubmitButton loginBn;

    private String userName;
    private String password;

    LoginModel mLoginModel;

    @Override
    public int attachLayoutRes() {
        return R.layout.login_activity;
    }

    @Override
    public void initViews() {
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mLoginModel = ModelProvider.getModel(this, LoginModel.class);

        mLoginModel.getLoginStatus().observe(this, new Observer<StatusDataResource>() {
            @Override
            public void onChanged(@Nullable StatusDataResource statusDataResource) {
                switch (statusDataResource.status) {
                    case ERROR:
                        onGetTokenError(statusDataResource.message);
                        break;
                    case SUCCESS:
                        onGetTokenSuccess((BasicToken) statusDataResource.data);
                        break;
                }
            }
        });

        mLoginModel.getUserStatus().observe(this, new Observer<StatusDataResource>() {
            @Override
            public void onChanged(@Nullable StatusDataResource statusDataResource) {
                switch (statusDataResource.status) {
                    case ERROR:
                        showShortToast(ToastType.ERROR, statusDataResource.message);
                        break;
                    case SUCCESS:
                        onLoginComplete();
                        break;
                }
            }
        });
    }


    @OnClick(R.id.login_bn)
    public void onLoginClick() {
        if (loginCheck()) {
            loginBn.setEnabled(false);
            mLoginModel.login(userName, password);
        } else {
            loginBn.reset();
        }
    }

    boolean loginCheck() {
        boolean valid = true;
        userName = userNameEt.getText().toString();
        password = passwordEt.getText().toString();
        if (BaseUtils.isBlank(userName)) {
            valid = false;
            userNameLayout.setError(getString(R.string.user_name_warning));
        } else {
            userNameLayout.setErrorEnabled(false);
        }
        if (BaseUtils.isBlank(password)) {
            valid = false;
            passwordLayout.setError(getString(R.string.password_warning));
        } else {
            passwordLayout.setErrorEnabled(false);
        }
        return valid;
    }


    void onGetTokenSuccess(BasicToken basicToken) {
        loginBn.doResult(true);
        mLoginModel.getUserInfo(basicToken);
    }

    void onGetTokenError(String errorMsg) {
        loginBn.doResult(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginBn.reset();
                loginBn.setEnabled(true);
            }
        }, 1000);

        showShortToast(ToastType.ERROR, errorMsg);
    }

    void onLoginComplete() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
