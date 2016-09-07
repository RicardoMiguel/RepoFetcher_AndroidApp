package com.service;

import com.model.github.Repo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ricar on 06/09/2016.
 */
public interface BitBucketService {
    @GET("repositories/{user}/")
    Observable<List<Repo>> listRepositories(@Path("user") String user);
}
