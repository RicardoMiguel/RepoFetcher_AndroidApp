package com.service;

import android.support.annotation.NonNull;

import com.model.Repo;

import java.util.List;

/**
 * Created by ricar on 04/09/2016.
 */
public interface RepoService {
    public void callListRepositories(@NonNull String user, @NonNull RepoServiceResponse<List<Repo>> callback);
}
