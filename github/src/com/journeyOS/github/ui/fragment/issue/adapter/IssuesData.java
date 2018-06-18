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

import android.os.Parcel;
import android.os.Parcelable;

import com.journeyOS.base.adapter.BaseAdapterData;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.User;
import com.journeyOS.github.type.IssueState;

import java.util.Date;

public class IssuesData implements BaseAdapterData, Parcelable {

    public int number;

    public String title;

    public int commentNum;

    public User user;

    public Date createdAt;

    public boolean isUserIssues;

    public String repoFullName;

    public String repoName;

    public String repoAuthorName;

    public IssueState state;

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
                ", repoName='" + repoName + '\'' +
                ", repoAuthorName='" + repoAuthorName + '\'' +
                ", state=" + state +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.number);
        dest.writeString(this.title);
        dest.writeInt(this.commentNum);
        dest.writeParcelable(this.user, flags);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeByte(this.isUserIssues ? (byte) 1 : (byte) 0);
        dest.writeString(this.repoFullName);
        dest.writeString(this.repoName);
        dest.writeString(this.repoAuthorName);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
    }

    public IssuesData() {
    }

    protected IssuesData(Parcel in) {
        this.number = in.readInt();
        this.title = in.readString();
        this.commentNum = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.isUserIssues = in.readByte() != 0;
        this.repoFullName = in.readString();
        this.repoName = in.readString();
        this.repoAuthorName = in.readString();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : IssueState.values()[tmpState];
    }

    public static final Creator<IssuesData> CREATOR = new Creator<IssuesData>() {
        @Override
        public IssuesData createFromParcel(Parcel source) {
            return new IssuesData(source);
        }

        @Override
        public IssuesData[] newArray(int size) {
            return new IssuesData[size];
        }
    };
}
