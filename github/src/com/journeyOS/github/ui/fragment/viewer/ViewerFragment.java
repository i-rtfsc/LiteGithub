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

import com.journeyOS.base.Constant;
import com.journeyOS.base.persistence.SpUtils;
import com.journeyOS.base.utils.GitHubHelper;
import com.journeyOS.base.utils.MarkdownHelper;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.files.adapter.ReposFileData;
import com.journeyOS.github.ui.widget.webview.CodeWebView;

import butterknife.BindView;
import io.github.kbiakov.codeview.CodeView;
import io.github.kbiakov.codeview.adapters.Options;
import io.github.kbiakov.codeview.highlight.ColorTheme;
import io.github.kbiakov.codeview.highlight.ColorThemeData;

public class ViewerFragment extends BaseFragment implements CodeWebView.ContentChangedListener {
    @BindView(R.id.web_view)
    CodeWebView webView;
    @BindView(R.id.code_view)
    CodeView codeView;
    @BindView(R.id.progress_bar)
    ProgressBar loader;

    ColorThemeData colorThemeData;

    static final String EXTRA_REPOS_FILE_DATA = "reposFileData";
    ReposFileData mReposFileData;

    ViewerModel mViewerModel;

    boolean wrap = false;

    final Observer<StatusDataResource> codeStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleCodeStatusObserver(statusDataResource);
        }
    };

    public static BaseFragment newInstance(@NonNull ReposFileData reposFileData) {
        ViewerFragment fragment = new ViewerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_REPOS_FILE_DATA, reposFileData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        boolean light = SpUtils.getInstant().getBoolean(Constant.THEME, true);
        colorThemeData = (light ? ColorTheme.DEFAULT.theme() : ColorTheme.MONOKAI.theme())
                .withBgContent(android.R.color.black)
                .withNoteColor(android.R.color.white);
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.viewer_fragment;
    }

    @Override
    public void initViews() {
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(true);

        mReposFileData = getArguments().getParcelable(EXTRA_REPOS_FILE_DATA);
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);
        mViewerModel = ModelProvider.getModel(this, ViewerModel.class);

        showLoading();
        mViewerModel.load(mReposFileData.url, mReposFileData.htmlUrl, mReposFileData.downloadUrl, false);

        mViewerModel.getCodeStatus().observe(this, codeStatusObserver);
    }

    void handleCodeStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                hideLoading();
                if (MarkdownHelper.isMarkdown(mReposFileData.url)) {
                    loadMdText((String) statusDataResource.data, mReposFileData.htmlUrl);
                } else {
                    String downloadUrl = mReposFileData.downloadUrl;
                    loadCode((String) statusDataResource.data, downloadUrl.substring(downloadUrl.lastIndexOf(".") + 1));
                }
                break;
            case ERROR:
                hideLoading();
                showShortToast(ToastType.ERROR, statusDataResource.message);
                break;
        }
    }

    void showLoading() {
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(true);
    }

    void hideLoading() {
        loader.setVisibility(View.GONE);
    }

    void loadMdText(@NonNull String text, @NonNull String baseUrl) {
        codeView.setVisibility(View.GONE);
        webView.setMdSource(text, baseUrl);
        webView.setContentChangedListener(this);
        webView.setVisibility(View.VISIBLE);
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(false);
    }

    void loadCode(@NonNull String text, @Nullable String language) {
        if (GitHubHelper.isSupportCode(language)) {
            webView.setVisibility(View.GONE);
            codeView.setOptions(Options.Default.get(mActivity)
                    .withCode(text)
                    .withLanguage(language)
                    .withTheme(colorThemeData));
            codeView.setVisibility(View.VISIBLE);
            getActivity().invalidateOptionsMenu();
        } else {
            webView.setCodeSource(text, wrap, MarkdownHelper.getExtension(mReposFileData.url));
            webView.setContentChangedListener(this);
            webView.setVisibility(View.GONE);
            getActivity().invalidateOptionsMenu();
            loader.setVisibility(View.VISIBLE);
            loader.setIndeterminate(false);
        }
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
