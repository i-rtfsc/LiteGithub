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

package com.journeyOS.core.http;

import com.google.gson.annotations.SerializedName;
import com.journeyOS.core.config.AppConfig;
import com.journeyOS.core.config.GithubConfig;

import java.util.Arrays;
import java.util.List;

public class AuthRequestModel {
    public List<String> scopes;
    public String note;
    public String noteUrl;
    @SerializedName("client_id")
    public String clientId;
    @SerializedName("client_secret")
    public String clientSecret;

    public static AuthRequestModel generate() {
        AuthRequestModel model = new AuthRequestModel();
        model.scopes = Arrays.asList("user", "repo", "gist", "notifications");
        model.note = AppConfig.APPLICATION_ID;
        model.clientId = GithubConfig.OPENHUB_CLIENT_ID;
        model.clientSecret = GithubConfig.OPENHUB_CLIENT_SECRET;
        model.noteUrl = GithubConfig.REDIRECT_URL;
        return model;
    }

    @Override
    public String toString() {
        return "AuthRequestModel{" +
                "scopes=" + scopes +
                ", note='" + note + '\'' +
                ", noteUrl='" + noteUrl + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                '}';
    }
}
