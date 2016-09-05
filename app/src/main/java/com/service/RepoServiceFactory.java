package com.service;

import android.support.annotation.Nullable;

/**
 * Created by ricar on 05/09/2016.
 */
class RepoServiceFactory {

    @Nullable
    IRepoServiceHandler create(@FetcherCallsHandler.RepoServiceType int service){
        switch (service){
            case FetcherCallsHandler.GITHUB:
                return new GitHubServiceHandler();
            case FetcherCallsHandler.BITBUCKET:
                return null;
        }
        return null;
    }
}