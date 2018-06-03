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

package com.journeyOS.github.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.journeyOS.github.type.IssueAuthorAssociation;
import com.journeyOS.github.type.IssueState;

import java.util.Date;

public class Issue implements Parcelable {

    public String id;

    public int number;

    public String title;

    public IssueState state;

    public boolean locked;

    @SerializedName("comments")
    public int commentNum;

    @SerializedName("created_at")
    public Date createdAt;

    @SerializedName("updated_at")
    public Date updatedAt;

    @SerializedName("closed_at")
    public Date closedAt;

    public String body;

    @SerializedName("body_html")
    public String bodyHtml;

    public User user;

    @SerializedName("author_association")
    public IssueAuthorAssociation authorAssociation;

    @SerializedName("repository_url")
    public String repoUrl;

    @SerializedName("html_url")
    public String htmlUrl;

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", number=" + number +
                ", title='" + title + '\'' +
                ", state=" + state +
                ", locked=" + locked +
                ", commentNum=" + commentNum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", closedAt=" + closedAt +
                ", body='" + body + '\'' +
                ", bodyHtml='" + bodyHtml + '\'' +
                ", user=" + user +
                ", authorAssociation=" + authorAssociation +
                ", repoUrl='" + repoUrl + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.number);
        dest.writeString(this.title);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeByte(this.locked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.commentNum);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeLong(this.closedAt != null ? this.closedAt.getTime() : -1);
        dest.writeString(this.body);
        dest.writeString(this.bodyHtml);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.authorAssociation == null ? -1 : this.authorAssociation.ordinal());
        dest.writeString(this.repoUrl);
        dest.writeString(this.htmlUrl);
    }

    public Issue() {
    }

    protected Issue(Parcel in) {
        this.id = in.readString();
        this.number = in.readInt();
        this.title = in.readString();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : IssueState.values()[tmpState];
        this.locked = in.readByte() != 0;
        this.commentNum = in.readInt();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        long tmpClosedAt = in.readLong();
        this.closedAt = tmpClosedAt == -1 ? null : new Date(tmpClosedAt);
        this.body = in.readString();
        this.bodyHtml = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        int tmpAuthorAssociation = in.readInt();
        this.authorAssociation = tmpAuthorAssociation == -1 ? null : IssueAuthorAssociation.values()[tmpAuthorAssociation];
        this.repoUrl = in.readString();
        this.htmlUrl = in.readString();
    }

    public static final Creator<Issue> CREATOR = new Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel source) {
            return new Issue(source);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };
}
