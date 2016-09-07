package com.service;

import android.support.annotation.NonNull;

import com.model.github.Repo;

import java.util.List;

/**
 * Created by ricar on 04/09/2016.
 */
public interface IRepoServiceHandler {
    void callListRepositories(@NonNull String user, @NonNull RepoServiceResponse<List<Repo>> callback);
}
