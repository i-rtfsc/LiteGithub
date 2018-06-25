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

import com.journeyOS.base.adapter.BaseAdapterData;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.Issue;
import com.journeyOS.github.entity.IssueCrossReferencedSource;
import com.journeyOS.github.entity.Label;
import com.journeyOS.github.entity.Milestone;
import com.journeyOS.github.entity.User;
import com.journeyOS.github.type.IssueEventType;

import java.util.Date;

public class IssueDetailsData implements BaseAdapterData {

    public IssueEventType type;

    public User user;

    public User actor;

    public User assignee;

    public String bodyHtml;

    public String body;

    public Issue parentIssue;

    public Date createdAt;

    public IssueCrossReferencedSource source;

    public Milestone milestone;

    public Label label;

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_comments;
    }

    @Override
    public String toString() {
        return "IssueDetailsData{" +
                "type=" + type +
                ", user=" + user +
                ", actor=" + actor +
                ", assignee=" + assignee +
                ", bodyHtml='" + bodyHtml + '\'' +
                ", body='" + body + '\'' +
                ", parentIssue=" + parentIssue +
                ", createdAt=" + createdAt +
                ", source=" + source +
                ", milestone=" + milestone +
                ", label=" + label +
                '}';
    }
}
