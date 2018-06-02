package com.journeyOS.github.api;

import android.support.annotation.NonNull;

import com.journeyOS.github.entity.FileModel;
import com.journeyOS.github.entity.Repository;
import com.journeyOS.github.entity.SearchResult;
import com.journeyOS.github.entity.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface GithubService {

    @NonNull @GET("user")
    Observable<Response<User>> getAuthUser(
            @Header("forceNetWork") boolean forceNetWork
    );

    @GET("users/{user}")
    Observable<Response<User>> getUser(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user
    );

    @GET("user{user}/repos")
    Observable<retrofit2.Response<ArrayList<Repository>>> getUserRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    @GET("user{user}/starred")
    Observable<Response<ArrayList<Repository>>> getUserStarred(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    @NonNull
    @GET("repos/{owner}/{repo}/contents/{path}")
    Observable<Response<ArrayList<FileModel>>> getRepoFiles(
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path(value = "path", encoded = true) String path,
            @Query("ref") String branch
    );

    @NonNull
    @GET
    @Headers("Accept: application/vnd.github.html")
    Observable<Response<ResponseBody>> getFileAsHtmlStream(
            @Header("forceNetWork") boolean forceNetWork,
            @Url String url
    );

    @NonNull
    @GET
    @Headers("Accept: application/vnd.github.VERSION.raw")
    Observable<Response<ResponseBody>> getFileAsStream(
            @Header("forceNetWork") boolean forceNetWork,
            @Url String url
    );

    @NonNull
    @GET("repos/{owner}/{repo}/stargazers")
    Observable<Response<ArrayList<User>>> getStargazers(
            @Header("forceNetWork") boolean forceNetWork,
            @Path(value = "owner") String owner,
            @Path(value = "repo") String repo,
            @Query("page") int page
    );

    @NonNull
    @GET("repos/{owner}/{repo}/subscribers")
    Observable<Response<ArrayList<User>>> getWatchers(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("page") int page
    );

    @NonNull
    @GET("users/{user}/followers")
    Observable<Response<ArrayList<User>>> getFollowers(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    @NonNull
    @GET("users/{user}/following")
    Observable<Response<ArrayList<User>>> getFollowing(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    @NonNull
    @GET("search/users")
    Observable<Response<SearchResult<User>>> searchUsers(
            @Query("q") String query,
            @Query("sort") String sort,
            @Query("order") String order,
            @Query("page") int page
    );

    @NonNull
    @GET("search/repositories")
    Observable<Response<SearchResult<Repository>>> searchRepos(
            @Query("q") String query,
            @Query("sort") String sort,
            @Query("order") String order,
            @Query("page") int page
    );

    @NonNull @GET("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> checkRepoStarred(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @PUT("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> starRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @DELETE("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> unstarRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @GET("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> checkRepoWatched(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @PUT("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> watchRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @DELETE("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> unwatchRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );
}
