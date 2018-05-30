package com.journeyOS.github.ui.fragment.repos.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.journeyOS.base.adapter.BaseAdapterData;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.User;

import java.util.Date;

public class RepositoryData implements BaseAdapterData, Parcelable {

    public String name;

    public String htmlUrl;

    public String defaultBranch;

    public String description;

    public int size;

    public int stargazersCount;

    public int forksCount;

    public User owner;

    public String language;

    public int openIssuesCount;

    public boolean hasIssues;

    public int subscribersCount;

    public boolean fork;

    public Date createdAt;

    public Date updatedAt;

    public Date pushedAt;

    public String fullName;

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_repository;
    }

    @Override
    public String toString() {
        return "RepositoryData{" +
                "name='" + name + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", defaultBranch='" + defaultBranch + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", stargazersCount=" + stargazersCount +
                ", forksCount=" + forksCount +
                ", owner=" + owner +
                ", language='" + language + '\'' +
                ", openIssuesCount=" + openIssuesCount +
                ", hasIssues=" + hasIssues +
                ", subscribersCount=" + subscribersCount +
                ", fork=" + fork +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", pushedAt=" + pushedAt +
                ", fullName='" + fullName + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.defaultBranch);
        dest.writeString(this.description);
        dest.writeInt(this.size);
        dest.writeInt(this.stargazersCount);
        dest.writeInt(this.forksCount);
        dest.writeParcelable(this.owner, flags);
        dest.writeString(this.language);
        dest.writeInt(this.openIssuesCount);
        dest.writeByte(this.hasIssues ? (byte) 1 : (byte) 0);
        dest.writeInt(this.subscribersCount);
        dest.writeByte(this.fork ? (byte) 1 : (byte) 0);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeLong(this.pushedAt != null ? this.pushedAt.getTime() : -1);
        dest.writeString(this.fullName);
    }

    public RepositoryData() {
    }

    protected RepositoryData(Parcel in) {
        this.name = in.readString();
        this.htmlUrl = in.readString();
        this.defaultBranch = in.readString();
        this.description = in.readString();
        this.size = in.readInt();
        this.stargazersCount = in.readInt();
        this.forksCount = in.readInt();
        this.owner = in.readParcelable(User.class.getClassLoader());
        this.language = in.readString();
        this.openIssuesCount = in.readInt();
        this.hasIssues = in.readByte() != 0;
        this.subscribersCount = in.readInt();
        this.fork = in.readByte() != 0;
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        long tmpPushedAt = in.readLong();
        this.pushedAt = tmpPushedAt == -1 ? null : new Date(tmpPushedAt);
        this.fullName = in.readString();
    }

    public static final Creator<RepositoryData> CREATOR = new Creator<RepositoryData>() {
        @Override
        public RepositoryData createFromParcel(Parcel source) {
            return new RepositoryData(source);
        }

        @Override
        public RepositoryData[] newArray(int size) {
            return new RepositoryData[size];
        }
    };
}
