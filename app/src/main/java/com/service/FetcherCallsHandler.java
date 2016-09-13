package com.service;

import android.accounts.NetworkErrorException;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.repofetcher.RepoFetcherApplication;
import com.service.request.ExchangeTokenRequest;
import com.service.request.ListRepositoriesRequest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

/**
 * Created by ricar on 04/09/2016.
 */
public class FetcherCallsHandler extends HashMap<Integer, RepoServiceHandler>{

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
    public RepoServiceHandler get(Object key) {

        RepoServiceHandler handler = super.get(key);

        if(handler == null){
            handler = new RepoServiceFactory().create((int)key);
            if(handler != null) {
                put((int) key, handler);
            }
        }

        return handler;
    }

    public static void callListRepositories(@RepoServiceType int service, @NonNull ListRepositoriesRequest<?> request){
        IRepoServiceHandler handler = getInstance().get(service);
        makeCallIfThereIsNetwork(() -> handler.callListRepositories(request), request.getServiceResponse());
    }

    public static void callExchangeToken(@RepoServiceType int service, @NonNull ExchangeTokenRequest<?> request){
        IRepoServiceHandler handler = getInstance().get(service);
        makeCallIfThereIsNetwork(() -> handler.exchangeToken(request), request.getServiceResponse());
    }

    @NonNull
    public static String getClientId(@RepoServiceType int service){
        return getInstance().get(service).getClientId();
    }

    @NonNull
    public static String getClientSecret(@RepoServiceType int service){
        return getInstance().get(service).getClientSecret();
    }

    @NonNull
    public static String getAuthorizationUrl(@RepoServiceType int service){
        return getInstance().get(service).getAuthorizationUrl();
    }

    private static void makeCallIfThereIsNetwork(@NonNull Runnable runnable, @NonNull RepoServiceResponse<?> callback){
        if(ServiceUtils.isNetworkAvailable(RepoFetcherApplication.getContext())){
            runnable.run();
        } else {
            SubscriberAdapter<?> subscriberAdapter = new SubscriberAdapter<>(callback);
            subscriberAdapter.onError(new NetworkErrorException());
        }
    }

    public static void unSubscribe(Fragment fragment){
        int id = System.identityHashCode(fragment);
        for (IRepoServiceHandler serviceHandler: getInstance().values())
        {
            if(serviceHandler instanceof SubscriberService){
                ((SubscriberService) serviceHandler).removeSubscribers(id);
            }
        }
    }
}
