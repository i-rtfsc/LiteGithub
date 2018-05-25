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

public class User implements Parcelable {
    public String login;

    public String id;

    public String name;

    @SerializedName("avatar_url")
    public String avatarUrl;

    @SerializedName("html_url")
    public String htmlUrl;

    public String type;

    public String company;

    public String blog;

    public String localtion;

    public String email;

    public String bio;

    @SerializedName("public_repos")
    public int publicRepos;

    @SerializedName("public_gists")
    public int publicGists;

    public int followers;

    public int following;

    @SerializedName("created_at")
    public Date createdAt;

    @SerializedName("updated_at")
    public Date updatedAt;

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", type='" + type + '\'' +
                ", company='" + company + '\'' +
                ", blog='" + blog + '\'' +
                ", localtion='" + localtion + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", publicRepos=" + publicRepos +
                ", publicGists=" + publicGists +
                ", followers=" + followers +
                ", following=" + following +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.login);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.type);
        dest.writeString(this.company);
        dest.writeString(this.blog);
        dest.writeString(this.localtion);
        dest.writeString(this.email);
        dest.writeString(this.bio);
        dest.writeInt(this.publicRepos);
        dest.writeInt(this.publicGists);
        dest.writeInt(this.followers);
        dest.writeInt(this.following);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.login = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.avatarUrl = in.readString();
        this.htmlUrl = in.readString();
        this.type = in.readString();
        this.company = in.readString();
        this.blog = in.readString();
        this.localtion = in.readString();
        this.email = in.readString();
        this.bio = in.readString();
        this.publicRepos = in.readInt();
        this.publicGists = in.readInt();
        this.followers = in.readInt();
        this.following = in.readInt();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
