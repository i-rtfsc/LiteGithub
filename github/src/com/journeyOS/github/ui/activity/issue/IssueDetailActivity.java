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

package com.journeyOS.github.ui.activity.issue;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyOS.core.CoreManager;
import com.journeyOS.core.ImageEngine;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.github.R;
import com.journeyOS.github.type.IssueState;
import com.journeyOS.github.ui.fragment.issue.adapter.IssuesData;
import com.journeyOS.github.ui.fragment.issue.detail.IssueDetailsFragment;

import butterknife.BindView;

public class IssueDetailActivity extends BaseActivity {
    static final String TAG = IssueDetailActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_avatar)
    ImageView userImageView;
    @BindView(R.id.issue_title)
    TextView issueTitle;
    @BindView(R.id.issue_state_img)
    ImageView issueStateImg;
    @BindView(R.id.issue_state_text)
    TextView issueStateText;

    Context mContext;

    static final String EXTRA_ISSUES_DATA = "issuesData";
    IssuesData mIssuesData;

    public static void show(@NonNull Context context, @NonNull IssuesData issuesData) {
        Intent intent = new Intent(context, IssueDetailActivity.class);
        intent.putExtra(EXTRA_ISSUES_DATA, issuesData);
        context.startActivity(intent);
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.issue_detail_activity;
    }

    @Override
    public void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIssuesData = getIntent().getParcelableExtra(EXTRA_ISSUES_DATA);
        showIssue(mIssuesData);
    }


    void showIssue(final IssuesData issuesData) {
        mToolbar.setTitle(mContext.getResources().getString(R.string.issues).concat(" #").concat(String.valueOf(issuesData.number)));
        issueTitle.setText(issuesData.title);

        ImageEngine.load(this, issuesData.user.avatarUrl, userImageView, R.mipmap.user);

        String commentStr = String.valueOf(issuesData.commentNum).concat(" ")
                .concat(getString(R.string.comments).toLowerCase());
        if (IssueState.open == issuesData.state) {
            issueStateImg.setImageResource(R.drawable.svg_issues);
            issueStateText.setText(getString(R.string.open).concat("    ").concat(commentStr));
        } else {
            issueStateImg.setImageResource(R.drawable.svg_issues_closed);
            issueStateText.setText(getString(R.string.closed).concat("    ").concat(commentStr));
        }

        loadFragment(IssueDetailsFragment.newInstance(issuesData));
    }

    void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
