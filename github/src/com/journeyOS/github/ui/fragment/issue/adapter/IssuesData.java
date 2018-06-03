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

import com.journeyOS.base.adapter.BaseAdapterData;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.User;

import java.util.Date;

public class IssuesData implements BaseAdapterData {

    public int number;

    public String title;

    public int commentNum;

    public User user;

    public Date createdAt;

    public boolean isUserIssues;

    public String repoFullName;

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_issue;
    }

    @Override
    public String toString() {
        return "IssuesData{" +
                "number=" + number +
                ", title='" + title + '\'' +
                ", commentNum=" + commentNum +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", isUserIssues=" + isUserIssues +
                ", repoFullName='" + repoFullName + '\'' +
                '}';
    }
}
