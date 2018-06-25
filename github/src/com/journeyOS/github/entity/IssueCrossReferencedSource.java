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

public class IssueCrossReferencedSource implements Parcelable {

    enum Type {
        issue, commit
    }

    public Type type;

    public Issue issue;

    @Override
    public String toString() {
        return "IssueCrossReferencedSource{" +
                "type=" + type +
                ", issue=" + issue +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeParcelable(this.issue, flags);
    }

    public IssueCrossReferencedSource() {
    }

    protected IssueCrossReferencedSource(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        this.issue = in.readParcelable(Issue.class.getClassLoader());
    }

    public static final Creator<IssueCrossReferencedSource> CREATOR = new Creator<IssueCrossReferencedSource>() {
        @Override
        public IssueCrossReferencedSource createFromParcel(Parcel source) {
            return new IssueCrossReferencedSource(source);
        }

        @Override
        public IssueCrossReferencedSource[] newArray(int size) {
            return new IssueCrossReferencedSource[size];
        }
    };
}
