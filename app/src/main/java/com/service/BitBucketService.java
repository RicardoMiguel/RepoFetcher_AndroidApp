package com.service;

import com.model.bitbucket.BitBucketRepositories;
import com.model.github.GitHubAccessToken;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by ricar on 06/09/2016.
 */
public interface BitBucketService {
    @GET("repositories/{user}/")
    Observable<BitBucketRepositories> listRepositories(@Path("user") String user);

    @POST
    Observable<GitHubAccessToken> exchangeToken(@Url String url, @QueryMap Map<String, String> params);
}
