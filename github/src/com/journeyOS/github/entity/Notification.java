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

public class Notification implements Parcelable {

    public enum Type {
        @SerializedName("updated_at") Assign, //You were assigned to the Issue.
        @SerializedName("author") Author, //You created the thread.
        @SerializedName("comment") Comment, //You commented on the thread.
        @SerializedName("invitation") Invitation, //You accepted an invitation to contribute to the repository.
        @SerializedName("manual") Manual, //You subscribed to the thread (via an Issue or Pull Request).
        @SerializedName("mention") Mention, //You were specifically @mentioned in the content.
        @SerializedName("state_change") StateChange, //You changed the thread state (for example, closing an Issue or merging a Pull Request).
        @SerializedName("subscribed") Subscribed, //You're watching the repository.
        @SerializedName("team_mention") TeamMention //You were on a team that was mentioned.
    }

    public String id;

    public boolean unread;

    public Type reason;

    @SerializedName("updated_at")
    public Date updateAt;

    @SerializedName("last_read_at")
    public Date lastReadAt;

    public Repository repository;

    public NotificationSubject subject;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByte(this.unread ? (byte) 1 : (byte) 0);
        dest.writeInt(this.reason == null ? -1 : this.reason.ordinal());
        dest.writeLong(this.updateAt != null ? this.updateAt.getTime() : -1);
        dest.writeLong(this.lastReadAt != null ? this.lastReadAt.getTime() : -1);
        dest.writeParcelable(this.repository, flags);
        dest.writeParcelable(this.subject, flags);
    }

    public Notification() {
    }

    protected Notification(Parcel in) {
        this.id = in.readString();
        this.unread = in.readByte() != 0;
        int tmpReason = in.readInt();
        this.reason = tmpReason == -1 ? null : Type.values()[tmpReason];
        long tmpUpdateAt = in.readLong();
        this.updateAt = tmpUpdateAt == -1 ? null : new Date(tmpUpdateAt);
        long tmpLastReadAt = in.readLong();
        this.lastReadAt = tmpLastReadAt == -1 ? null : new Date(tmpLastReadAt);
        this.repository = in.readParcelable(Repository.class.getClassLoader());
        this.subject = in.readParcelable(NotificationSubject.class.getClassLoader());
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
