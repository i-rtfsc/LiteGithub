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

package com.journeyOS.github.ui.fragment.notification.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.utils.UIUtils;
import com.journeyOS.github.R;
import com.journeyOS.github.type.NotificationSubjectType;

import butterknife.BindView;

public class NotificationHolder extends BaseViewHolder<NotificationData> {

    @BindView(R.id.type_icon)
    AppCompatImageView typeIcon;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.status)
    AppCompatImageView status;

    public NotificationHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(NotificationData data, int position) {
        if (data != null) {
            if (data.unread) {
                status.setImageResource(R.drawable.svg_unread);
            } else {
                status.setVisibility(View.INVISIBLE);
            }
            title.setText(data.title);
            time.setText(data.time);

            int padding = UIUtils.dip2px(getContext(), 2);
            if (NotificationSubjectType.Issue.equals(data.subjectType)) {
                typeIcon.setImageResource(R.drawable.svg_issues);
            } else if (NotificationSubjectType.Commit.equals(data.subjectType)) {
                typeIcon.setImageResource(R.drawable.svg_commit);
            } else {
                typeIcon.setImageResource(R.drawable.svg_pull);
            }
            typeIcon.setPadding(padding, padding, padding, padding);
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_notification;
    }

}
