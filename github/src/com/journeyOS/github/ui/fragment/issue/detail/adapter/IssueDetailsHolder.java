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

package com.journeyOS.github.ui.fragment.issue.detail.adapter;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.core.ImageEngine;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.Label;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class IssueDetailsHolder extends BaseViewHolder<IssueDetailsData> {
    @BindView(R.id.user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.comment_desc)
    TextView commentDesc;
    @BindView(R.id.issue_labels)
    TextView issueLabels;

    public IssueDetailsHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(IssueDetailsData data, int position) {
        if (!BaseUtils.isNull(data.parentIssue)) {
            issueLabels.setVisibility(View.VISIBLE);
            issueLabels.setText(data.parentIssue.title);
        } else {
            issueLabels.setVisibility(View.GONE);
        }

        switch (data.type) {
            case commented:
                ImageEngine.load(getContext(), data.user.avatarUrl, userAvatar, R.mipmap.user);
                userName.setText(data.user.login);
                time.setText(BaseUtils.getNewsTimeStr(getContext(), data.createdAt));
                if (!BaseUtils.isBlank(data.bodyHtml)) {
                    commentDesc.setText(Html.fromHtml(data.bodyHtml));
                } else if (!BaseUtils.isBlank(data.bodyHtml)) {
                    commentDesc.setText(data.bodyHtml);
                } else {
                    commentDesc.setText(R.string.no_description);
                }
                break;
            default:
                ImageEngine.load(getContext(), data.actor.avatarUrl, userAvatar, R.mipmap.user);
                userName.setText(data.actor.login);
                time.setText(BaseUtils.getNewsTimeStr(getContext(), data.createdAt));
                setDesc(data);
                break;
        }

    }

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_comments;
    }

    void setDesc(IssueDetailsData detailsData) {
        SpannableStringBuilder text = new SpannableStringBuilder(detailsData.actor.login);
        text.append(" ");
        String info;
        switch (detailsData.type) {
            case reopened:
                text.append(getContext().getString(R.string.issue_reopened));
                break;
            case closed:
                text.append(getContext().getString(R.string.issue_close));
                break;
            case renamed:
                text.append(getContext().getString(R.string.issue_modified));
                break;
            case locked:
                text.append(getContext().getString(R.string.issue_locked_conversation));
                break;
            case unlocked:
                text.append(getContext().getString(R.string.issue_unlocked_conversation));
                break;
            case crossReferenced:
                if (detailsData.source.type != null) {
                    info = String.format(getContext().getString(R.string.issue_referenced),
                            "#" + detailsData.source.issue.title);
                    text.append(info);
                }
                break;
            case assigned:
                info = String.format(getContext().getString(R.string.issue_assigned), detailsData.assignee.login);
                text.append(info);
                break;
            case unassigned:
                text.append(getContext().getString(R.string.issue_unassigned));
                break;
            case milestoned:
                info = String.format(getContext().getString(R.string.issue_added_to_milestone),
                        detailsData.milestone.title);
                text.append(info);
                break;
            case demilestoned:
                info = String.format(getContext().getString(R.string.issue_removed_from_milestone),
                        detailsData.milestone.title);
                text.append(info);
                break;
            case commentDeleted:
                text.append(getContext().getString(R.string.issue_delete_comment));
                break;
            case labeled:
                info = String.format(getContext().getString(R.string.issue_add_label), "[label]");
                text.append(info);
                break;
            case unlabeled:
                info = String.format(getContext().getString(R.string.issue_remove_label), "[label]");
                text.append(info);
                break;
            default:
                break;
        }

        int labelPos = text.toString().indexOf("[label]");
        Label label = detailsData.label;
        if (label != null && labelPos >= 0) {
            text.replace(labelPos, labelPos + 7, label.name);
        }

        String timeStr = BaseUtils.getNewsTimeStr(getContext(), detailsData.createdAt);
        text.append(" ").append(timeStr);
        commentDesc.setText(text);
    }

}
