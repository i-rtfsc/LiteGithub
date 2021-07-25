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

package com.journeyOS.github.ui.activity.viewer;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.files.adapter.ReposFileData;
import com.journeyOS.github.ui.fragment.viewer.ViewerFragment;

import butterknife.BindView;

public class ViewerActivity extends BaseActivity {
    static final String EXTRA_REPOS_FILE_DATA = "reposFileData";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static void show(@NonNull Context context, @NonNull ReposFileData reposFileData) {
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtra(EXTRA_REPOS_FILE_DATA, reposFileData);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.viewer_activity;
    }

    @Override
    public void initViews() {
        ReposFileData reposFileData = getIntent().getParcelableExtra(EXTRA_REPOS_FILE_DATA);

        mToolbar.setTitle(reposFileData.name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BaseFragment fragment = ViewerFragment.newInstance(reposFileData);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
