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

package com.journeyOS.github.ui.activity;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.journeyOS.base.utils.UIUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.plugins.ISettingsProvider;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.filter.IssuesFilter;
import com.journeyOS.github.type.FragmentType;
import com.journeyOS.github.type.RepoType;
import com.journeyOS.github.type.UserType;
import com.journeyOS.github.ui.fragment.issue.IssuesFragment;
import com.journeyOS.github.ui.fragment.repos.ReposFragment;
import com.journeyOS.github.ui.fragment.user.UserFragment;

import butterknife.BindView;

public class ContainerActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    static final String EXTRA_FRAGMENT_TYPE = "fragmentType";

    static final String EXTRA_REPOS_TYPE = "reposType";

    static final String EXTRA_USER_TYPE = "usersType";
    static final String EXTRA_USER = "user";
    static final String EXTRA_REPO = "repo";

    static final String EXTRA_ISSUES_FILTER = "issuesFilter";

    public static void show(@NonNull Context context, @NonNull FragmentType fragmentType, @NonNull RepoType repoType, @NonNull String user,
                            @NonNull String repo) {
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_TYPE, fragmentType);
        intent.putExtra(EXTRA_REPOS_TYPE, repoType);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_REPO, repo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showUser(@NonNull Context context, @NonNull FragmentType fragmentType, @NonNull UserType userType, @NonNull String user,
                                @NonNull String repo) {
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_TYPE, fragmentType);
        intent.putExtra(EXTRA_USER_TYPE, userType);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_REPO, repo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showSettings(@NonNull Context context, @NonNull FragmentType fragmentType) {
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_TYPE, fragmentType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showForUser(@NonNull Context context, @NonNull FragmentType fragmentType, @NonNull IssuesFilter issuesFilter) {
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_TYPE, fragmentType);
        intent.putExtra(EXTRA_ISSUES_FILTER, issuesFilter);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.container_activity;
    }

    @Override
    public void initViews() {
        UIUtils.setStatusBarColor(this, this.getResources().getColor(R.color.colorPrimary));
        int title = -1;
        FragmentType fragmentType = (FragmentType) getIntent().getSerializableExtra(EXTRA_FRAGMENT_TYPE);
        String user = null;
        String repo = null;
        switch (fragmentType) {
            case USER:
                UserType userType = (UserType) getIntent().getSerializableExtra(EXTRA_USER_TYPE);
                user = getIntent().getStringExtra(EXTRA_USER);
                repo = getIntent().getStringExtra(EXTRA_REPO);

                if (userType == UserType.FOLLOWERS) {
                    title = R.string.followers;
                } else if (userType == UserType.FOLLOWING) {
                    title = R.string.following;
                } else if (userType == UserType.WATCHERS) {
                    title = R.string.watchers;
                } else if (userType == UserType.STARGAZERS) {
                    title = R.string.stargazers;
                }
                loadFragment(UserFragment.newInstance(userType, user, repo), title);
                break;
            case REPOS:
                RepoType repoType = (RepoType) getIntent().getSerializableExtra(EXTRA_REPOS_TYPE);
                user = getIntent().getStringExtra(EXTRA_USER);
                repo = getIntent().getStringExtra(EXTRA_REPO);
                if (repoType == RepoType.OWNED) {
                    title = R.string.my_repos;
                } else if (repoType == RepoType.STARRED) {
                    title = R.string.starred;
                } else if (repoType == RepoType.PUBLIC) {
                    title = R.string.my_repos;
                }
                loadFragment(ReposFragment.newInstance(repoType, user, repo), title);
                break;
            case SETTINGS:
                title = R.string.settings;
                loadFragment(CoreManager.getImpl(ISettingsProvider.class).provideSettingsFragment(this), title);
                break;
            case ISSUE:
                title = R.string.issues;
                IssuesFilter issuesFilter = (IssuesFilter) getIntent().getSerializableExtra(EXTRA_ISSUES_FILTER);
                loadFragment(IssuesFragment.newInstanceForUser(issuesFilter), title);
                break;
        }
    }

    void loadFragment(Fragment fragment, int title) {
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }
}
