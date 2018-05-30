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

public class Repository {

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
}
