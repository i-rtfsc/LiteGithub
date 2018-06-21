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

import java.util.Date;

public class Milestone implements Parcelable {

    public enum State {
        OPEN, CLOSED
    }

    public long id;

    public int number;

    public String title;

    public String description;

    public User creator;

    @SerializedName("open_issues")
    public int openIssues;

    @SerializedName("closed_issues")
    public int closedIssues;

    public Milestone.State state;

    @SerializedName("created_at")
    public Date createdAt;

    @SerializedName("updated_at")
    public Date updatedAt;

    @SerializedName("due_on")
    public Date dueOn;

    @SerializedName("closed_at")
    public Date closedAt;


    @Override
    public String toString() {
        return "Milestone{" +
                "id=" + id +
                ", number=" + number +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creator=" + creator +
                ", openIssues=" + openIssues +
                ", closedIssues=" + closedIssues +
                ", state=" + state +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", dueOn=" + dueOn +
                ", closedAt=" + closedAt +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.number);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeParcelable(this.creator, flags);
        dest.writeInt(this.openIssues);
        dest.writeInt(this.closedIssues);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeLong(this.dueOn != null ? this.dueOn.getTime() : -1);
        dest.writeLong(this.closedAt != null ? this.closedAt.getTime() : -1);
    }

    public Milestone() {
    }

    protected Milestone(Parcel in) {
        this.id = in.readLong();
        this.number = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.creator = in.readParcelable(User.class.getClassLoader());
        this.openIssues = in.readInt();
        this.closedIssues = in.readInt();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : State.values()[tmpState];
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        long tmpDueOn = in.readLong();
        this.dueOn = tmpDueOn == -1 ? null : new Date(tmpDueOn);
        long tmpClosedAt = in.readLong();
        this.closedAt = tmpClosedAt == -1 ? null : new Date(tmpClosedAt);
    }

    public static final Creator<Milestone> CREATOR = new Creator<Milestone>() {
        @Override
        public Milestone createFromParcel(Parcel source) {
            return new Milestone(source);
        }

        @Override
        public Milestone[] newArray(int size) {
            return new Milestone[size];
        }
    };
}
