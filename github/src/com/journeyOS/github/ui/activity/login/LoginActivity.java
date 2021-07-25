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

package com.journeyOS.github.ui.activity.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.ToastyUtils;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.config.GithubConfig;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.BasicToken;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.github_login_btn)
    Button loginBn;

    LoginModel mLoginModel;

    Dialog githubdialog;

    final Observer<StatusDataResource> loginStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleLoginStatusObserver(statusDataResource);
        }
    };
    final Observer<StatusDataResource> userStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleUserStatusObserver(statusDataResource);
        }
    };

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

        mLoginModel.getLoginStatus().observe(this, loginStatusObserver);
        mLoginModel.getUserStatus().observe(this, userStatusObserver);
    }


    void handleLoginStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                onGetTokenSuccess((BasicToken) statusDataResource.data);
                break;
            case ERROR:
                onGetTokenError(statusDataResource.message);
                break;
        }
    }

    void handleUserStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                onLoginComplete();
                break;
            case ERROR:
                showToast(ToastyUtils.ToastType.ERROR, statusDataResource.message, false);
                break;
        }
    }

    @OnClick(R.id.github_login_btn)
    public void onLoginClick() {
        openWebView(this);
    }

    protected void openWebView(Context context) {
        String authUrl = mLoginModel.getAuthUrl();
        LogUtils.d(LoginModel.TAG, "open webview, auth url = [" + authUrl + "]");
        githubdialog = new Dialog(context);
        WebView webView = new WebView(context);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setWebViewClient(new GithubWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(authUrl);
        githubdialog.setContentView(webView);
        githubdialog.show();
    }


    void onGetTokenSuccess(BasicToken basicToken) {
        mLoginModel.getUserInfo(basicToken);
    }

    void onGetTokenError(String errorMsg) {
        showToast(ToastyUtils.ToastType.ERROR, errorMsg, false);
    }

    void onLoginComplete() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    class GithubWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            LogUtils.d(LoginModel.TAG, "should override url loading, url = [" + url + "]");
            if (url.startsWith(GithubConfig.REDIRECT_URL)) {
                mLoginModel.handleUrl(url);

                if (url.contains("code=")) {
                    githubdialog.dismiss();
                }

                return true;
            }

            return false;
        }
    }
}
