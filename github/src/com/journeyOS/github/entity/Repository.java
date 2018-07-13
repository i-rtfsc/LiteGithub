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

import java.util.Date;

public class Repository implements Parcelable{

    public int id;

    public String name;

    @SerializedName("full_name")
    public String fullName;

    @SerializedName("private")
    public boolean repPrivate;

    @SerializedName("html_url")
    public String htmlUrl;

    public String description;

    public String language;

    public User owner;

    @SerializedName("default_branch")
    public String defaultBranch;

    @SerializedName("created_at")
    public Date createdAt;

    @SerializedName("updated_at")
    public Date updatedAt;

    @SerializedName("pushed_at")
    public Date pushedAt;

    @SerializedName("git_url")
    public String gitUrl;

    @SerializedName("ssh_url")
    public String sshUrl;

    @SerializedName("clone_url")
    public String cloneUrl;

    @SerializedName("svn_url")
    public String svnUrl;

    public int size;

    @SerializedName("stargazers_count")
    public int stargazersCount;

    @SerializedName("watchers_count")
    public int watchersCount;

    @SerializedName("forks_count")
    public int forksCount;

    @SerializedName("open_issues_count")
    public int openIssuesCount;

    public boolean fork;

//    public Repository parent;

    public RepositoryPermissions permissions;

    @SerializedName("has_issues")
    public boolean hasIssues;

    @SerializedName("has_projects")
    public boolean hasProjects;

    @SerializedName("has_downloads")
    public boolean hasDownloads;

    @SerializedName("has_wiki")
    public boolean hasWiki;

    @SerializedName("has_pages")
    public boolean hasPages;

    @SerializedName("subscribers_count")
    public int subscribersCount;

    @Override
    public String toString() {
        return "Repository{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", repPrivate=" + repPrivate +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", owner=" + owner +
                ", defaultBranch='" + defaultBranch + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", pushedAt=" + pushedAt +
                ", gitUrl='" + gitUrl + '\'' +
                ", sshUrl='" + sshUrl + '\'' +
                ", cloneUrl='" + cloneUrl + '\'' +
                ", svnUrl='" + svnUrl + '\'' +
                ", size=" + size +
                ", stargazersCount=" + stargazersCount +
                ", watchersCount=" + watchersCount +
                ", forksCount=" + forksCount +
                ", openIssuesCount=" + openIssuesCount +
                ", fork=" + fork +
                ", permissions=" + permissions +
                ", hasIssues=" + hasIssues +
                ", hasProjects=" + hasProjects +
                ", hasDownloads=" + hasDownloads +
                ", hasWiki=" + hasWiki +
                ", hasPages=" + hasPages +
                ", subscribersCount=" + subscribersCount +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.fullName);
        dest.writeByte(this.repPrivate ? (byte) 1 : (byte) 0);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.description);
        dest.writeString(this.language);
        dest.writeParcelable(this.owner, flags);
        dest.writeString(this.defaultBranch);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeLong(this.pushedAt != null ? this.pushedAt.getTime() : -1);
        dest.writeString(this.gitUrl);
        dest.writeString(this.sshUrl);
        dest.writeString(this.cloneUrl);
        dest.writeString(this.svnUrl);
        dest.writeInt(this.size);
        dest.writeInt(this.stargazersCount);
        dest.writeInt(this.watchersCount);
        dest.writeInt(this.forksCount);
        dest.writeInt(this.openIssuesCount);
        dest.writeByte(this.fork ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.permissions, flags);
        dest.writeByte(this.hasIssues ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasProjects ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasDownloads ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasWiki ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasPages ? (byte) 1 : (byte) 0);
        dest.writeInt(this.subscribersCount);
    }

    public Repository() {
    }

    protected Repository(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.fullName = in.readString();
        this.repPrivate = in.readByte() != 0;
        this.htmlUrl = in.readString();
        this.description = in.readString();
        this.language = in.readString();
        this.owner = in.readParcelable(User.class.getClassLoader());
        this.defaultBranch = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        long tmpPushedAt = in.readLong();
        this.pushedAt = tmpPushedAt == -1 ? null : new Date(tmpPushedAt);
        this.gitUrl = in.readString();
        this.sshUrl = in.readString();
        this.cloneUrl = in.readString();
        this.svnUrl = in.readString();
        this.size = in.readInt();
        this.stargazersCount = in.readInt();
        this.watchersCount = in.readInt();
        this.forksCount = in.readInt();
        this.openIssuesCount = in.readInt();
        this.fork = in.readByte() != 0;
        this.permissions = in.readParcelable(RepositoryPermissions.class.getClassLoader());
        this.hasIssues = in.readByte() != 0;
        this.hasProjects = in.readByte() != 0;
        this.hasDownloads = in.readByte() != 0;
        this.hasWiki = in.readByte() != 0;
        this.hasPages = in.readByte() != 0;
        this.subscribersCount = in.readInt();
    }

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel source) {
            return new Repository(source);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };
}
