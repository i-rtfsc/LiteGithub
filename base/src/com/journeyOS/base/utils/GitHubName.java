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

package com.journeyOS.base.utils;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class GitHubName {
    private String url;
    private String userName;
    private String repoName;

    public static GitHubName fromUrl(@NonNull String url) {
        if (!GitHubHelper.isGitHubUrl(url)) return null;
        GitHubName gitHubName = new GitHubName();
        url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        gitHubName.url = url;
        try {
            Uri uri = Uri.parse(url);
            ArrayList<String> list = new ArrayList<>(uri.getPathSegments());
            list.remove("repos");
            if (list.size() > 0) gitHubName.userName = list.get(0);
            if (list.size() > 1) gitHubName.repoName = list.get(1);
        } catch (Exception e) {

        }
        return gitHubName;
    }

    public String getUserName() {
        return userName;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getReleaseTagName() {
        if (!GitHubHelper.isReleaseTagUrl(url)) {
            return null;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public String getCommitShaName() {
        if (!GitHubHelper.isCommitUrl(url)) {
            return null;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
