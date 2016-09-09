package com.service;

import android.accounts.NetworkErrorException;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.repofetcher.RepoFetcherApplication;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

/**
 * Created by ricar on 04/09/2016.
 */
public class FetcherCallsHandler extends HashMap<Integer, IRepoServiceHandler>{

    public static final int GITHUB = 0;
    public static final int BITBUCKET = 1;

    @IntDef({GITHUB, BITBUCKET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepoServiceType {}

    @Nullable //The instance might be null. Use getInstance instead.
    private static FetcherCallsHandler instance;

    @NonNull
    private static FetcherCallsHandler getInstance(){
        if(instance == null){
            instance = new FetcherCallsHandler();
        }
        return instance;
    }

    private FetcherCallsHandler(){
    }

    @Override
    public IRepoServiceHandler get(Object key) {

        IRepoServiceHandler handler = super.get(key);

        if(handler == null){
            handler = new RepoServiceFactory().create((int)key);
            if(handler != null) {
                put((int) key, handler);
            }
        }

        return handler;
    }

    public static void callListRepositories(int hash, @RepoServiceType int service, @NonNull String user, @NonNull RepoServiceResponse<?> callback){
        IRepoServiceHandler handler = getInstance().get(service);
        makeCallIfThereIsNetwork(() -> handler.callListRepositories(hash, user, callback), callback);
    }

    private static void makeCallIfThereIsNetwork(@NonNull Runnable runnable, @NonNull RepoServiceResponse<?> callback){
        if(ServiceUtils.isNetworkAvailable(RepoFetcherApplication.getContext())){
            runnable.run();
        } else {
            SubscriberAdapter<?> subscriberAdapter = new SubscriberAdapter<>(callback);
            subscriberAdapter.onError(new NetworkErrorException());
        }
    }

    public static void unSubscribe(int id){
        for (IRepoServiceHandler serviceHandler: getInstance().values())
        {
            if(serviceHandler instanceof SubscriberService){
                ((SubscriberService) serviceHandler).removeSubscribers(id);
            }
        }
    }
}
