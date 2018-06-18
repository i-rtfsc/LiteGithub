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

public class IssueUtils {

    public static String getRepoName(String repoUrl) {
        return (!BaseUtils.isBlank(repoUrl) && repoUrl.contains("/")) ?
                repoUrl.substring(repoUrl.lastIndexOf("/") + 1) : null;

    }

    public static String getRepoFullName(String repoUrl) {
        return (!BaseUtils.isBlank(repoUrl) && repoUrl.contains("repos/")) ?
                repoUrl.substring(repoUrl.lastIndexOf("repos/") + 6) : null;

    }

    public static String getRepoAuthorName(String repoUrl) {
        return (!BaseUtils.isBlank(repoUrl) && repoUrl.contains("repos/")) ?
                repoUrl.substring(repoUrl.lastIndexOf("repos/") + 6, repoUrl.lastIndexOf("/")) : null;
    }
}
