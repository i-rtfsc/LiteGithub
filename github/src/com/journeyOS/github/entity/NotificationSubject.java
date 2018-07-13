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

import com.journeyOS.github.type.NotificationSubjectType;

public class NotificationSubject implements Parcelable {

    public String title;

    public String url;

    public NotificationSubjectType type;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    }

    public NotificationSubject() {
    }

    protected NotificationSubject(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : NotificationSubjectType.values()[tmpType];
    }

    public static final Creator<NotificationSubject> CREATOR = new Creator<NotificationSubject>() {
        @Override
        public NotificationSubject createFromParcel(Parcel source) {
            return new NotificationSubject(source);
        }

        @Override
        public NotificationSubject[] newArray(int size) {
            return new NotificationSubject[size];
        }
    };
}
