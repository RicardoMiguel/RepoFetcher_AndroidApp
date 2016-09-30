package com.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.service.oauth.OAuthClientManager;
import com.service.oauth.OAuthClientRequester;

/**
 * Created by ricar on 05/09/2016.
 */
class RepoServiceFactory {

    private Context context;
    private OAuthClientManager requester;


    public RepoServiceFactory(@NonNull Context context, @Nullable OAuthClientManager clientManager) {
        this.context = context;
        this.requester = clientManager;
    }

    @Nullable
    RepoServiceHandler create(@FetcherCallsHandler.RepoServiceType int service){
        switch (service){
            case FetcherCallsHandler.GITHUB:
                return new GitHubServiceHandler(context, requester);
            case FetcherCallsHandler.BITBUCKET:
                return new BitBucketServiceHandler(context, requester);
        }
        return null;
    }
}
