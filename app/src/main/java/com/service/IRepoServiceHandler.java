package com.service;

import android.support.annotation.NonNull;

/**
 * Created by ricar on 04/09/2016.
 */
public interface IRepoServiceHandler {
    void callListRepositories(int hash, @NonNull String user, @NonNull RepoServiceResponse<?> callback);
}
