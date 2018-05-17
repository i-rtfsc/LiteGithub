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

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
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
}
