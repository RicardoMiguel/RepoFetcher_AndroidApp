package com.service;

import android.support.annotation.NonNull;

import com.service.request.ListRepositoriesRequest;

/**
 * Created by ricar on 04/09/2016.
 */
public interface IRepoServiceHandler {
    void callListRepositories(@NonNull ListRepositoriesRequest<?> request);
}
