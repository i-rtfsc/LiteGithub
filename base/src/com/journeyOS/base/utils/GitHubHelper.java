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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import java.util.regex.Pattern;

public class GitHubHelper {

    private static final String GITHUB_BASE_URL_PATTERN_STR = "(https://)?(http://)?(www.)?github.com";

    private static final Pattern GITHUB_URL_PATTERN = Pattern.compile(GITHUB_BASE_URL_PATTERN_STR + "(.)*");

    private static final Pattern RELEASE_TAG_PATTERN = Pattern.compile(GITHUB_BASE_URL_PATTERN_STR
            + "/([a-z]|[A-Z]|\\d|-)*/([a-z]|[A-Z]|\\d|-|\\.|_)*/releases/tag/([^/])*(/)?");

    private static final Pattern COMMIT_PATTERN = Pattern.compile(GITHUB_BASE_URL_PATTERN_STR
            + "/([a-z]|[A-Z]|\\d|-)*/([a-z]|[A-Z]|\\d|-|\\.|_)*/commit(s)?/([a-z]|\\d)*(/)?");

    private static final String[] IMAGE_EXTENSIONS = {".png", ".jpg", ".jpeg", ".gif", ".svg"};

    private static final String[] CODE_EXTENSIONS = {"c", "cc", "cpp", "cxx", "cyc", "m",
            "cs", "coffee", "rc", "rs", "rust", "go", "hs", "java", "javascript", "js",
            "erlang", "erl", "bash", "bsh", "csh", "sh", "cv", "py", "python", "clj",
            "css", "vhdl", "vhd", "perl", "pl", "pm", "basic", "cbm", "xq", "xquery",
            "rb", "ruby", "apollo", "agc", "aea", "cl", "el", "lisp", "lsp", "scm", "ss",
            "rkt", "llvm", "ll", "lua", "matlab", "latex", "tex", "json", "xml"};


    public static boolean isGitHubUrl(@NonNull String url) {
        return GITHUB_URL_PATTERN.matcher(url).matches();
    }

    public static boolean isReleaseTagUrl(@NonNull String url) {
        return RELEASE_TAG_PATTERN.matcher(url).matches();
    }

    public static boolean isCommitUrl(@NonNull String url) {
        return COMMIT_PATTERN.matcher(url).matches();
    }

    public static boolean isImage(@Nullable String name) {
        if (BaseUtils.isBlank(name)) return false;
        name = name.toLowerCase();
        for (String value : IMAGE_EXTENSIONS) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(name);
            if ((extension != null && value.replace(".", "").equals(extension))
                    || name.endsWith(value))
                return true;
        }
        return false;
    }

    public static boolean isSupportCode(@Nullable String name) {
        if (BaseUtils.isBlank(name)) return false;
        name = name.toLowerCase();
        for (String value : CODE_EXTENSIONS) {
            if (value.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
