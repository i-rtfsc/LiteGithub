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

package com.journeyOS.github.ui.fragment.files.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.journeyOS.base.adapter.BaseAdapterData;
import com.journeyOS.github.R;

public class ReposFileData implements BaseAdapterData, Parcelable {

    public String name;

    public int size;

    public String path;

    public boolean isFile;

    public boolean isDir;

    public String url;

    public String htmlUrl;

    public String downloadUrl;

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_file;
    }

    @Override
    public String toString() {
        return "ReposFileData{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", path='" + path + '\'' +
                ", isFile=" + isFile +
                ", isDir=" + isDir +
                ", url='" + url + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.size);
        dest.writeString(this.path);
        dest.writeByte(this.isFile ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDir ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.downloadUrl);
    }

    public ReposFileData() {
    }

    protected ReposFileData(Parcel in) {
        this.name = in.readString();
        this.size = in.readInt();
        this.path = in.readString();
        this.isFile = in.readByte() != 0;
        this.isDir = in.readByte() != 0;
        this.url = in.readString();
        this.htmlUrl = in.readString();
        this.downloadUrl = in.readString();
    }

    public static final Creator<ReposFileData> CREATOR = new Creator<ReposFileData>() {
        @Override
        public ReposFileData createFromParcel(Parcel source) {
            return new ReposFileData(source);
        }

        @Override
        public ReposFileData[] newArray(int size) {
            return new ReposFileData[size];
        }
    };
}
