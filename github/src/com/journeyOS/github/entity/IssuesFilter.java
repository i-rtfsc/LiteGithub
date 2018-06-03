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

import com.journeyOS.github.type.IssueState;
import com.journeyOS.github.type.IssueType;
import com.journeyOS.github.type.IssuesSortType;
import com.journeyOS.github.type.SortDirection;
import com.journeyOS.github.type.UserIssuesFilterType;

public class IssuesFilter implements Parcelable {

    public IssueType issueType;

    public IssueState issueState;

    public UserIssuesFilterType userIssuesFilterType = UserIssuesFilterType.ALL;

    public IssuesSortType issuesSortType = IssuesSortType.CREATED;

    public SortDirection sortDirection = SortDirection.DESC;


    @Override
    public String toString() {
        return "IssuesFilter{" +
                "issueType=" + issueType +
                ", issueState=" + issueState +
                ", userIssuesFilterType=" + userIssuesFilterType +
                ", issuesSortType=" + issuesSortType +
                ", sortDirection=" + sortDirection +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.issueType == null ? -1 : this.issueType.ordinal());
        dest.writeInt(this.issueState == null ? -1 : this.issueState.ordinal());
        dest.writeInt(this.userIssuesFilterType == null ? -1 : this.userIssuesFilterType.ordinal());
        dest.writeInt(this.issuesSortType == null ? -1 : this.issuesSortType.ordinal());
        dest.writeInt(this.sortDirection == null ? -1 : this.sortDirection.ordinal());
    }

    public IssuesFilter() {
    }

    public IssuesFilter(IssueType issueType, IssueState issueState) {
        this.issueType = issueType;
        this.issueState = issueState;
    }

    protected IssuesFilter(Parcel in) {
        int tmpIssueType = in.readInt();
        this.issueType = tmpIssueType == -1 ? null : IssueType.values()[tmpIssueType];
        int tmpIssueState = in.readInt();
        this.issueState = tmpIssueState == -1 ? null : IssueState.values()[tmpIssueState];
        int tmpUserIssuesFilterType = in.readInt();
        this.userIssuesFilterType = tmpUserIssuesFilterType == -1 ? null : UserIssuesFilterType.values()[tmpUserIssuesFilterType];
        int tmpIssuesSortType = in.readInt();
        this.issuesSortType = tmpIssuesSortType == -1 ? null : IssuesSortType.values()[tmpIssuesSortType];
        int tmpSortDirection = in.readInt();
        this.sortDirection = tmpSortDirection == -1 ? null : SortDirection.values()[tmpSortDirection];
    }

    public static final Creator<IssuesFilter> CREATOR = new Creator<IssuesFilter>() {
        @Override
        public IssuesFilter createFromParcel(Parcel source) {
            return new IssuesFilter(source);
        }

        @Override
        public IssuesFilter[] newArray(int size) {
            return new IssuesFilter[size];
        }
    };
}
