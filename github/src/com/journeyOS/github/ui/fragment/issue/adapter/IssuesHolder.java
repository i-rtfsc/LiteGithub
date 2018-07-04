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

package com.journeyOS.github.ui.fragment.issue.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.ImageEngine;
import com.journeyOS.github.R;

import butterknife.BindView;

public class IssuesHolder extends BaseViewHolder<IssuesData> {
    static final String TAG = IssuesHolder.class.getSimpleName();
    @BindView(R.id.user_avatar)
    ImageView userAvatar;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.issue_title)
    TextView issueTitle;
    @BindView(R.id.comment_num)
    TextView commentNum;
    @BindView(R.id.repo_full_name)
    TextView repoFullName;

    public IssuesHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(IssuesData data, int position) {
        ImageEngine.load(CoreManager.getContext(), data.user.avatarUrl, userAvatar, R.mipmap.user);
        userName.setText(data.user.login);
        issueTitle.setText(data.title);
        commentNum.setText(String.valueOf(data.commentNum));
        time.setText(BaseUtils.getNewsTimeStr(CoreManager.getContext(), data.createdAt));
        if (data.isUserIssues) {
            repoFullName.setText(data.repoFullName.concat(" #").concat(String.valueOf(data.number)));
        } else {
            repoFullName.setText(("#").concat(String.valueOf(data.number)));
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_issue;
    }

}
