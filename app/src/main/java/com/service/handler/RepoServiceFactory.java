package com.service.handler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.service.holder.RepoServiceType;
import com.service.oauth.OAuthClientManager;

/**
 * Created by ricar on 05/09/2016.
 */
public class RepoServiceFactory {

    private Context context;
    private OAuthClientManager requester;


    public RepoServiceFactory(@NonNull Context context, @Nullable OAuthClientManager clientManager) {
        this.context = context;
        this.requester = clientManager;
    }

    @Nullable
    public RepoServiceHandler create(@RepoServiceType int service){
        switch (service){
            case RepoServiceType.GITHUB:
                return new GitHubServiceHandler(context, requester);
            case RepoServiceType.BITBUCKET:
                return new BitBucketServiceHandler(context, requester);
        }
        return null;
    }
}
