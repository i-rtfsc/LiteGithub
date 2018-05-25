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

public class FileModel {
    public String name;

    public String path;

    public String sha;

    public int size;

    public String url;

    @SerializedName("html_url")
    public String htmlUrl;

    @SerializedName("git_url")
    public String gitUrl;

    @SerializedName("download_url")
    public String downloadUrl;

    @SerializedName("type")
    public String type;

    @Override
    public String toString() {
        return "FileModel{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", sha='" + sha + '\'' +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", gitUrl='" + gitUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
