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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.UIUtils;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.repos.ReposFragment;
import com.journeyOS.github.ui.fragment.user.UserFragment;

import butterknife.BindView;

public class ContainerActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    static final String EXTRA_REPOS_TYPE = "reposType";

    static final String EXTRA_USER_TYPE = "usersType";
    static final String EXTRA_USER = "user";
    static final String EXTRA_REPO = "repo";

    public static void show(@NonNull Context context, @NonNull ReposFragment.ReposType reposType) {
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(EXTRA_REPOS_TYPE, reposType);
        context.startActivity(intent);
    }

    public static void showUser(@NonNull Context context, @NonNull UserFragment.UsersType usersType, @NonNull String user,
                                @NonNull String repo) {
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(EXTRA_USER_TYPE, usersType);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_REPO, repo);
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
        ReposFragment.ReposType reposType = (ReposFragment.ReposType) getIntent().getSerializableExtra(EXTRA_REPOS_TYPE);
        if (!BaseUtils.isNull(reposType)) {
            if (reposType == ReposFragment.ReposType.OWNED) {
                title = R.string.my_repos;
            } else if (reposType == ReposFragment.ReposType.OWNED) {
                title = R.string.starred;
            }

            loadFragment(ReposFragment.newInstance(reposType), title);
        }

        UserFragment.UsersType usersType = (UserFragment.UsersType) getIntent().getSerializableExtra(EXTRA_USER_TYPE);
        if (!BaseUtils.isNull(usersType)) {
            String user = getIntent().getStringExtra(EXTRA_USER);
            String repo = getIntent().getStringExtra(EXTRA_REPO);

            if (usersType == UserFragment.UsersType.FOLLOWERS) {
                title = R.string.followers;
            } else if (usersType == UserFragment.UsersType.FOLLOWING) {
                title = R.string.following;
            } else if (usersType == UserFragment.UsersType.WATCHERS) {
                title = R.string.watchers;
            } else if (usersType == UserFragment.UsersType.STARGAZERS) {
                title = R.string.stargazers;
            }
            loadFragment(UserFragment.newInstance(usersType, user, repo), title);
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
