package com.journeyOS.github.ui.fragment.repos.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.journeyOS.base.adapter.BaseAdapterData;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.User;

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
