package com.journeyOS.github.api;

import android.support.annotation.NonNull;

import com.journeyOS.github.entity.FileModel;
import com.journeyOS.github.entity.Repository;
import com.journeyOS.github.entity.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface GithubService {

    @GET("user{user}")
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

    @NonNull @GET @Headers("Accept: application/vnd.github.html")
    Observable<Response<ResponseBody>> getFileAsHtmlStream(
            @Header("forceNetWork") boolean forceNetWork,
            @Url String url
    );

    @NonNull @GET @Headers("Accept: application/vnd.github.VERSION.raw")
    Observable<Response<ResponseBody>> getFileAsStream(
            @Header("forceNetWork") boolean forceNetWork,
            @Url String url
    );
}
