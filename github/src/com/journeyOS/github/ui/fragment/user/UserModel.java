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

package com.journeyOS.github.ui.fragment.user;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.StatusDataResource;
import com.journeyOS.core.http.AppHttpClient;
import com.journeyOS.core.http.HttpCoreManager;
import com.journeyOS.core.http.HttpObserver;
import com.journeyOS.core.http.HttpResponse;
import com.journeyOS.core.viewmodel.BaseViewModel;
import com.journeyOS.github.BuildConfig;
import com.journeyOS.github.api.GithubService;
import com.journeyOS.github.entity.SearchResult;
import com.journeyOS.github.entity.User;
import com.journeyOS.github.ui.activity.search.SearchFilter;
import com.journeyOS.github.ui.fragment.user.adapter.UserData;

import java.util.ArrayList;

import retrofit2.Response;
import rx.Observable;

public class UserModel extends BaseViewModel {
    static final boolean DEBUG = BuildConfig.DEBUG;
    static final String TAG = UserModel.class.getSimpleName();

    MutableLiveData<StatusDataResource> mUsersStatus = new MutableLiveData<>();

    protected MutableLiveData<StatusDataResource> getUsersStatus() {
        return mUsersStatus;
    }

    MutableLiveData<StatusDataResource> mSearchUsersStatus = new MutableLiveData<>();

    protected MutableLiveData<StatusDataResource> getSearchUsersStatus() {
        return mSearchUsersStatus;
    }

    GithubService mGithubService;

    @Override
    protected void onCreate() {
        mGithubService = AppHttpClient.getInstance(CoreManager.getAccessToken()).getService(GithubService.class);
    }

    void loadUsers(final UserFragment.UsersType usersType, final String user, final String repo, final int page, final boolean isReload) {
        final boolean readCacheFirst = page == 1 && !isReload;
        HttpObserver<ArrayList<User>> httpObserver =
                new HttpObserver<ArrayList<User>>() {
                    @Override
                    public void onSuccess(HttpResponse<ArrayList<User>> response) {
                        LogUtils.d(TAG, "load user success");
                        ArrayList<UserData> userDataArrayList = convertFromUser((ArrayList<User>) response.body());
                        mUsersStatus.postValue(StatusDataResource.success(userDataArrayList, page));
                    }

                    @Override
                    public void onError(Throwable error) {
                        LogUtils.d(TAG, "load user error = " + error.getMessage());
                        mUsersStatus.postValue(StatusDataResource.error(error.getMessage()));
                    }
                };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<ArrayList<User>,
                Response<ArrayList<User>>>() {
            @Nullable
            @Override
            public Observable<Response<ArrayList<User>>> createObservable(boolean forceNetWork) {
                switch (usersType) {
                    case WATCHERS:
                        return mGithubService.getWatchers(forceNetWork, user, repo, page);
                    case FOLLOWERS:
                        return mGithubService.getFollowers(forceNetWork, user, page);
                    case FOLLOWING:
                        return mGithubService.getFollowing(forceNetWork, user, page);
                    case STARGAZERS:
                        return mGithubService.getStargazers(forceNetWork, user, repo, page);
                    default:
                        throw new IllegalArgumentException(usersType.name());
                }
            }
        }, httpObserver, readCacheFirst);
    }

    void searchUsers(final SearchFilter searchFilter, final int page) {
        HttpObserver<SearchResult<User>> httpObserver =
                new HttpObserver<SearchResult<User>>() {
                    @Override
                    public void onSuccess(HttpResponse<SearchResult<User>> response) {
                        LogUtils.d(TAG, "search use success");
                        ArrayList<UserData> userDataArrayList = convertFromUser((ArrayList<User>) response.body().items);
                        mSearchUsersStatus.postValue(StatusDataResource.success(userDataArrayList, page));
                    }

                    @Override
                    public void onError(Throwable error) {
                        LogUtils.d(TAG, "search search error = " + error.getMessage());
                        mSearchUsersStatus.postValue(StatusDataResource.error(error.getMessage()));
                    }
                };

        HttpCoreManager.executeRxHttp(new HttpCoreManager.IObservableCreator<SearchResult<User>,
                Response<SearchResult<User>>>() {
            @Nullable
            @Override
            public Observable<Response<SearchResult<User>>> createObservable(boolean forceNetWork) {
                return mGithubService.searchUsers(searchFilter.getQuery(), searchFilter.getSort(), searchFilter.getOrder(), page);
            }
        }, httpObserver);
    }

    ArrayList<UserData> convertFromUser(ArrayList<User> users) {
        ArrayList<UserData> userDataArrayList = new ArrayList<>();
        for (User user : users) {
            UserData userData = new UserData();
            userData.login = user.login;
            userData.avatarUrl = user.avatarUrl;
            userDataArrayList.add(userData);
            if (DEBUG)
                LogUtils.d(TAG, "convert from User, source data = " + users.toString());
            if (DEBUG)
                LogUtils.d(TAG, "convert from User, target data = " + userData.toString());
        }

        return userDataArrayList;
    }
}
