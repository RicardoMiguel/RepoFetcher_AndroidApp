package com.service;

import com.model.Repo;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by ricar on 02/09/2016.
 */
interface GitHubService extends IService{
    @GET("users/{user}/repos")
    Observable<List<Repo>> listRepositories(@Path("user") String user, @QueryMap Map<String, String> params);
}

