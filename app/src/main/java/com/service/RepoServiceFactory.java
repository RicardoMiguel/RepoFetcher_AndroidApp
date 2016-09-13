package com.service;

import android.support.annotation.Nullable;

/**
 * Created by ricar on 05/09/2016.
 */
class RepoServiceFactory {

    @Nullable
    RepoServiceHandler create(@FetcherCallsHandler.RepoServiceType int service){
        switch (service){
            case FetcherCallsHandler.GITHUB:
                return new GitHubServiceHandler();
            case FetcherCallsHandler.BITBUCKET:
                return new BitBucketServiceHandler();
        }
        return null;
    }
}
