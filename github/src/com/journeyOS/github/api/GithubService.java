package com.journeyOS.github.api;

import android.support.annotation.NonNull;

import com.journeyOS.github.entity.FileModel;
import com.journeyOS.github.entity.Issue;
import com.journeyOS.github.entity.IssueEvent;
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

    @NonNull
    @GET("user")
    Observable<Response<User>> getAuthUser(
            @Header("forceNetWork") boolean forceNetWork
    );

    @GET("users/{user}")
    Observable<Response<User>> getUser(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user
    );

    @NonNull
    @GET("user/repos")
    Observable<retrofit2.Response<ArrayList<Repository>>> getUserRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Query("page") int page,
            @Query("type") String type,
            @Query("sort") String sort,
            @Query("direction") String direction
    );

    @NonNull
    @GET("users/{user}/repos")
    Observable<retrofit2.Response<ArrayList<Repository>>> getUserPublicRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") @NonNull String user,
            @Query("page") int page,
            @Query("type") String type,
            @Query("sort") String sort,
            @Query("direction") String direction
    );

    @NonNull
    @GET("users/{user}/starred")
    Observable<Response<ArrayList<Repository>>> getUserStarred(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") @NonNull String user,
            @Query("page") int page,
            @Query("sort") String sort,
            @Query("direction") String direction
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

    //https://developer.github.com/v3/search/#search-repositories
    //https://api.github.com/search/repositories?q=tetris+language:assembly&sort=stars&order=desc
    @NonNull
    @GET("search/repositories")
    Observable<Response<SearchResult<Repository>>> searchRepos(
            @Query("q") String query,
            @Query("sort") String sort,
            @Query("order") String order,
            @Query("page") int page
    );

    @NonNull
    @GET("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> checkRepoStarred(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @PUT("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> starRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @DELETE("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> unstarRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @GET("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> checkRepoWatched(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @PUT("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> watchRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @DELETE("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> unwatchRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull
    @GET("search/issues")
    @Headers("Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    Observable<Response<SearchResult<Issue>>> searchIssues(
            @Header("forceNetWork") boolean forceNetWork,
            @Query(value = "q", encoded = true) String query,
            @Query("sort") String sort,
            @Query("order") String order,
            @Query("page") int page
    );

    @NonNull
    @GET("repos/{owner}/{repo}/issues")
    @Headers("Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    Observable<Response<ArrayList<Issue>>> getRepoIssues(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("state") String state,
            @Query("sort") String sort,
            @Query("direction") String direction,
            @Query("page") int page
    );

    @NonNull
    @GET("repos/{owner}/{repo}/issues/{issueNumber}/timeline?per_page=60")
    @Headers("Accept: application/vnd.github.mockingbird-preview,application/vnd.github.html," +
            " application/vnd.github.VERSION.raw,application/vnd.github.squirrel-girl-preview")
    Observable<Response<ArrayList<IssueEvent>>> getIssueTimeline(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path("issueNumber") int issueNumber,
            @Query("page") int page
    );

}
