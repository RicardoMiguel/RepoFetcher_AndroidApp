package com.service;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.Repo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ricar on 04/09/2016.
 */
public class FetcherCallsHandler extends HashMap<Integer, IRepoServiceHandler>{

    public static final int GITHUB = 0;
    public static final int BITBUCKET = 1;

    @IntDef({GITHUB, BITBUCKET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepoServiceType {}

    private static FetcherCallsHandler instance;

    private static FetcherCallsHandler getInstance(){
        if(instance == null){
            instance = new FetcherCallsHandler();
        }
        return instance;
    }

    public static void callListRepositories(@RepoServiceType int service, @NonNull String user, @NonNull RepoServiceResponse<List<Repo>> callback){
        IRepoServiceHandler handler = getInstance().get(service);
        handler.callListRepositories(user, callback);
    }


    @Nullable
    private IRepoServiceHandler chooseService(@RepoServiceType int service){
        switch (service){
            case GITHUB:
                return new GitHubServiceHandler();
            case BITBUCKET:
                return null;
        }
        return null;
    }

    @Override
    public IRepoServiceHandler get(Object key) {

        IRepoServiceHandler handler = super.get(key);

        if(handler == null){
            handler = chooseService((int)key);
            put((int)key, handler);
        }

        return handler;
    }
}
