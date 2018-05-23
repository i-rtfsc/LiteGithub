/*
 *
 *  * Copyright (c) 2018 anqi.huang@outlook.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.journeyOS.github.ui.fragment.viewer;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.journeyOS.base.utils.MarkdownHelper;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.widget.webview.CodeWebView;

import butterknife.BindView;

public class ViewerFragment extends BaseFragment implements CodeWebView.ContentChangedListener {
    @BindView(R.id.web_view)
    CodeWebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar loader;

    static String mUrl;
    static String mHtmlUrl;
    static String mDownloadUrl;

    ViewerModel mViewerModel;

    boolean wrap = false;

    public static BaseFragment newInstance(@NonNull String url, @NonNull String htmlUrl, @NonNull String downloadUrl) {
        ViewerFragment fragment = new ViewerFragment();
        mUrl = url;
        mHtmlUrl = htmlUrl;
        mDownloadUrl = downloadUrl;
        return fragment;
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.viewer_fragment;
    }

    @Override
    public void initViews() {
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(true);
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mViewerModel = ModelProvider.getModel(this, ViewerModel.class);

        showLoading();
        mViewerModel.load(mUrl, mHtmlUrl, mDownloadUrl, false);

        mViewerModel.getCodeStatus().observe(this, new Observer<StatusDataResource>() {
            @Override
            public void onChanged(@Nullable StatusDataResource statusDataResource) {
                switch (statusDataResource.status) {
                    case SUCCESS:
                        hideLoading();
                        if (MarkdownHelper.isMarkdown(mUrl)) {
                            loadMdText((String) statusDataResource.data, mHtmlUrl);
                        } else {
                            loadCode((String) statusDataResource.data, MarkdownHelper.getExtension(mUrl));
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showShortToast(ToastType.ERROR, statusDataResource.message);
                        break;
                }
            }
        });
    }

    void showLoading() {
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(true);
    }

    void hideLoading() {
        loader.setVisibility(View.GONE);
    }

    void loadMdText(@NonNull String text, @NonNull String baseUrl) {
        webView.setMdSource(text, baseUrl);
        webView.setContentChangedListener(this);
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(false);
    }

    void loadCode(@NonNull String text, @Nullable String extension) {
        webView.setCodeSource(text, wrap, extension);
        webView.setContentChangedListener(this);
        getActivity().invalidateOptionsMenu();
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(false);
    }

    @Override
    public void onContentChanged(int progress) {
        if (loader != null) {
            loader.setProgress(progress);
            if (progress == 100) {
                loader.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onScrollChanged(boolean reachedTop, int scroll) {

    }
}
