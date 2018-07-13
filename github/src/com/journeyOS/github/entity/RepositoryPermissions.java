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

public class RepositoryPermissions implements Parcelable {

    public boolean admin;

    public boolean push;

    public boolean pull;

    @Override
    public String toString() {
        return "RepositoryPermissions{" +
                "admin=" + admin +
                ", push=" + push +
                ", pull=" + pull +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.admin ? (byte) 1 : (byte) 0);
        dest.writeByte(this.push ? (byte) 1 : (byte) 0);
        dest.writeByte(this.pull ? (byte) 1 : (byte) 0);
    }

    public RepositoryPermissions() {
    }

    protected RepositoryPermissions(Parcel in) {
        this.admin = in.readByte() != 0;
        this.push = in.readByte() != 0;
        this.pull = in.readByte() != 0;
    }

    public static final Parcelable.Creator<RepositoryPermissions> CREATOR = new Parcelable.Creator<RepositoryPermissions>() {
        @Override
        public RepositoryPermissions createFromParcel(Parcel source) {
            return new RepositoryPermissions(source);
        }

        @Override
        public RepositoryPermissions[] newArray(int size) {
            return new RepositoryPermissions[size];
        }
    };
}
