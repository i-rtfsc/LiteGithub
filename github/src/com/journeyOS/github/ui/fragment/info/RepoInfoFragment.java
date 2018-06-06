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

package com.journeyOS.github.ui.fragment.info;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.ToastyUtils;
import com.journeyOS.base.utils.ViewUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.config.GithubConfig;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.github.R;
import com.journeyOS.github.type.FragmentType;
import com.journeyOS.github.type.UserType;
import com.journeyOS.github.ui.activity.ContainerActivity;
import com.journeyOS.github.ui.activity.profile.ProfileActivity;
import com.journeyOS.github.ui.activity.viewer.RepositoryActivity;
import com.journeyOS.github.ui.fragment.repos.adapter.RepositoryData;
import com.journeyOS.github.ui.widget.webview.CodeWebView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

public class RepoInfoFragment extends BaseFragment {

    @BindView(R.id.scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.repo_title_text)
    TextView repoTitleText;
    @BindView(R.id.fork_info_text)
    TextView forkInfoText;
    @BindView(R.id.repo_created_info_text)
    TextView repoCreatedInfoText;
    @BindView(R.id.issues_num_text)
    TextView issuesNumText;
    @BindView(R.id.issues_lay)
    View issueLay;
    @BindView(R.id.stargazers_num_text)
    TextView stargazersNumText;
    @BindView(R.id.forks_num_text)
    TextView forksNumText;
    @BindView(R.id.watchers_num_text)
    TextView watchersNumText;

    @BindView(R.id.readme_title)
    TextView readmeTitle;

    @BindView(R.id.web_view)
    CodeWebView webView;

    private boolean isReadmeSetted = false;

    Context mContext;

    static final String EXTRA_REPOSITORY_DATA = "repositoryData";
    RepositoryData mRepositoryData;

    String baseUrl;
    RepoInfoModel mRepoInfoModel;
    final Observer<StatusDataResource> repoInfoStatusObserver = new Observer<StatusDataResource>() {
        @Override
        public void onChanged(@Nullable StatusDataResource statusDataResource) {
            handleReposInfoStatusObserver(statusDataResource);
        }
    };

    public static Fragment newInstance(@NonNull RepositoryData repositoryData) {
        RepoInfoFragment fragment = new RepoInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_REPOSITORY_DATA, repositoryData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.repo_info_fragment;
    }

    @Override
    protected void initDataObserver(Bundle savedInstanceState) {
        super.initDataObserver(savedInstanceState);

        String curBranch = mRepositoryData.defaultBranch;
        String readmeFileUrl = GithubConfig.GITHUB_API_BASE_URL + "repos/" + mRepositoryData.fullName
                + "/" + "readme" + (BaseUtils.isBlank(curBranch) ? "" : "?ref=" + curBranch);

        String branch = BaseUtils.isBlank(curBranch) ? mRepositoryData.defaultBranch : curBranch;
        baseUrl = GithubConfig.GITHUB_BASE_URL + mRepositoryData.fullName
                + "/blob/" + branch + "/" + "README.md";

        mRepoInfoModel = ModelProvider.getModel(this, RepoInfoModel.class);
        mRepoInfoModel.loadReadMe(readmeFileUrl);
        mRepoInfoModel.getReposInfoStatus().observe(this, repoInfoStatusObserver);

    }

    @Override
    public void initViews() {
        mRepositoryData = getArguments().getParcelable(EXTRA_REPOSITORY_DATA);

        issueLay.setVisibility(mRepositoryData.hasIssues ? View.VISIBLE : View.GONE);
        issuesNumText.setText(String.valueOf(mRepositoryData.openIssuesCount));
        stargazersNumText.setText(String.valueOf(mRepositoryData.stargazersCount));
        forksNumText.setText(String.valueOf(mRepositoryData.forksCount));
        watchersNumText.setText(String.valueOf(mRepositoryData.subscribersCount));

        String createStr = (mRepositoryData.fork ? getString(R.string.forked_at)
                : getString(R.string.created_at)) + " " + BaseUtils.getDateStr(mRepositoryData.createdAt);
        if (mRepositoryData.pushedAt != null) {
            String updateStr = getString(R.string.latest_commit) + " "
                    + BaseUtils.getNewsTimeStr(getActivity(), mRepositoryData.pushedAt);
            repoCreatedInfoText.setText(String.format("%s, %s", createStr, updateStr));
        } else {
            repoCreatedInfoText.setText(createStr);
        }

        String fullName = mRepositoryData.fullName;
        SpannableStringBuilder spannable = new SpannableStringBuilder(fullName);
        spannable.setSpan(new ForegroundColorSpan(ViewUtils.getAccentColor(getContext())),
                0, fullName.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ProfileActivity.show(mContext, mRepositoryData.owner.login);
            }

            @Override
            public void updateDrawState(TextPaint ds) {

            }
        }, 0, fullName.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        repoTitleText.setMovementMethod(LinkMovementMethod.getInstance());
        repoTitleText.setText(spannable);
    }

    @OnClick({R.id.issues_lay, R.id.stargazers_lay, R.id.froks_lay, R.id.watchers_lay,
            R.id.fork_info_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.issues_lay:
                showToast(ToastyUtils.ToastType.INFO, R.string.developing, false);
                break;
            case R.id.stargazers_lay:
                if (mRepositoryData.stargazersCount == 0) return;
                ContainerActivity.showUser(mContext, FragmentType.USER, UserType.STARGAZERS, mRepositoryData.owner.login, mRepositoryData.name);
                break;
            case R.id.froks_lay:
                if (mRepositoryData.forksCount == 0) return;
                showToast(ToastyUtils.ToastType.INFO, R.string.developing, false);
                break;
            case R.id.watchers_lay:
                if (mRepositoryData.subscribersCount == 0) return;
                ContainerActivity.showUser(mContext, FragmentType.USER, UserType.WATCHERS, mRepositoryData.owner.login, mRepositoryData.name);
                break;
            case R.id.fork_info_text:
                RepositoryActivity.show(getActivity(), mRepositoryData);
                break;
        }
    }

    void handleReposInfoStatusObserver(StatusDataResource statusDataResource) {
        switch (statusDataResource.status) {
            case SUCCESS:
                try {
                    showReadMe(((ResponseBody) statusDataResource.data).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ERROR:
                showTipDialog(statusDataResource.message);
                break;
        }
    }

    void showReadMe(String source) {
        if (!isReadmeSetted) {
            isReadmeSetted = true;
            webView.setMdSource(source, baseUrl, true);
            webView.setVisibility(View.VISIBLE);
        }
    }
}
