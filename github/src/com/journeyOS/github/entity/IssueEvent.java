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
import com.journeyOS.github.type.IssueEventType;

import java.util.Date;

public class IssueEvent implements Parcelable {

    public String id;

    public User user;

    @SerializedName("created_at")
    public Date createdAt;

    @SerializedName("updated_at")
    public Date updatedAt;

    @SerializedName("author_association")
    public IssueAuthorAssociation authorAssociation;

    public String body;

    @SerializedName("body_html")
    public String bodyHtml;

    @SerializedName("event")
    public IssueEventType type;

    @SerializedName("html_url")
    public String htmlUrl;

    public User assignee;

    public User assigner;

    public User actor;

    public Label label;

    public Milestone milestone;

    public Reactions reactions;

    public IssueCrossReferencedSource source;

    public Issue parentIssue;

    @Override
    public String toString() {
        return "IssueEvent{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", authorAssociation=" + authorAssociation +
                ", body='" + body + '\'' +
                ", bodyHtml='" + bodyHtml + '\'' +
                ", type=" + type +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", assignee=" + assignee +
                ", assigner=" + assigner +
                ", actor=" + actor +
                ", label=" + label +
                ", milestone=" + milestone +
                ", reactions=" + reactions +
                ", source=" + source +
                ", parentIssue=" + parentIssue +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.user, flags);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeInt(this.authorAssociation == null ? -1 : this.authorAssociation.ordinal());
        dest.writeString(this.body);
        dest.writeString(this.bodyHtml);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.htmlUrl);
        dest.writeParcelable(this.assignee, flags);
        dest.writeParcelable(this.assigner, flags);
        dest.writeParcelable(this.actor, flags);
        dest.writeParcelable(this.label, flags);
        dest.writeParcelable(this.milestone, flags);
        dest.writeParcelable(this.reactions, flags);
        dest.writeParcelable(this.source, flags);
        dest.writeParcelable(this.parentIssue, flags);
    }

    public IssueEvent() {
    }

    protected IssueEvent(Parcel in) {
        this.id = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        int tmpAuthorAssociation = in.readInt();
        this.authorAssociation = tmpAuthorAssociation == -1 ? null : IssueAuthorAssociation.values()[tmpAuthorAssociation];
        this.body = in.readString();
        this.bodyHtml = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : IssueEventType.values()[tmpType];
        this.htmlUrl = in.readString();
        this.assignee = in.readParcelable(User.class.getClassLoader());
        this.assigner = in.readParcelable(User.class.getClassLoader());
        this.actor = in.readParcelable(User.class.getClassLoader());
        this.label = in.readParcelable(Label.class.getClassLoader());
        this.milestone = in.readParcelable(Milestone.class.getClassLoader());
        this.reactions = in.readParcelable(Reactions.class.getClassLoader());
        this.source = in.readParcelable(IssueCrossReferencedSource.class.getClassLoader());
        this.parentIssue = in.readParcelable(Issue.class.getClassLoader());
    }

    public static final Creator<IssueEvent> CREATOR = new Creator<IssueEvent>() {
        @Override
        public IssueEvent createFromParcel(Parcel source) {
            return new IssueEvent(source);
        }

        @Override
        public IssueEvent[] newArray(int size) {
            return new IssueEvent[size];
        }
    };
}
