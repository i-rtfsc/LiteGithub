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

import com.google.gson.annotations.SerializedName;

public class Reactions implements Parcelable {

    @SerializedName("total_count")
    public int totalCount;

    @SerializedName("+1")
    public int plusOne;

    @SerializedName("-1")
    public int minusOne;

    public int laugh;

    public int hooray;

    public int confused;

    public int heart;

    @Override
    public String toString() {
        return "Reactions{" +
                "totalCount=" + totalCount +
                ", plusOne=" + plusOne +
                ", minusOne=" + minusOne +
                ", laugh=" + laugh +
                ", hooray=" + hooray +
                ", confused=" + confused +
                ", heart=" + heart +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalCount);
        dest.writeInt(this.plusOne);
        dest.writeInt(this.minusOne);
        dest.writeInt(this.laugh);
        dest.writeInt(this.hooray);
        dest.writeInt(this.confused);
        dest.writeInt(this.heart);
    }

    public Reactions() {
    }

    protected Reactions(Parcel in) {
        this.totalCount = in.readInt();
        this.plusOne = in.readInt();
        this.minusOne = in.readInt();
        this.laugh = in.readInt();
        this.hooray = in.readInt();
        this.confused = in.readInt();
        this.heart = in.readInt();
    }

    public static final Creator<Reactions> CREATOR = new Creator<Reactions>() {
        @Override
        public Reactions createFromParcel(Parcel source) {
            return new Reactions(source);
        }

        @Override
        public Reactions[] newArray(int size) {
            return new Reactions[size];
        }
    };
}
