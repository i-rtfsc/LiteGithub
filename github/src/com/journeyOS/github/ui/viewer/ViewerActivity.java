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

package com.journeyOS.github.ui.viewer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.fragment.viewer.ViewerFragment;

import butterknife.BindView;

public class ViewerActivity extends BaseActivity {
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_THML_URL = "htmlUrl";
    public static final String EXTRA_DOWNLOAD_URL = "downloadUrl";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static void show(@NonNull Context context, @NonNull String name, @NonNull String url, @NonNull String htmlUrl, @NonNull String downloadUrl) {
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_THML_URL, htmlUrl);
        intent.putExtra(EXTRA_DOWNLOAD_URL, downloadUrl);
        context.startActivity(intent);
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.viewer_activity;
    }

    @Override
    public void initViews() {
        String name = getIntent().getStringExtra(EXTRA_NAME);
        String url = getIntent().getStringExtra(EXTRA_URL);
        String htmlUrl = getIntent().getStringExtra(EXTRA_THML_URL);
        String downloadUrl = getIntent().getStringExtra(EXTRA_DOWNLOAD_URL);

        mToolbar.setTitle(name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BaseFragment fragment = ViewerFragment.newInstance(url, htmlUrl, downloadUrl);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
