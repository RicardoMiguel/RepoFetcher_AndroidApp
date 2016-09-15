package com.service;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.model.github.GitHubAccessToken;
import com.service.request.ExchangeTokenRequest;
import com.service.request.ListRepositoriesRequest;
import com.service.rx.RxJavaController;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricar on 04/09/2016.
 */
public class FetcherCallsHandler extends HashMap<Integer, RepoServiceHandler> implements OAuthClientRequester{

    public static final int GITHUB = 0;
    public static final int BITBUCKET = 1;

    @IntDef({GITHUB, BITBUCKET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepoServiceType {}

    @Nullable //The instance might be null. Use getInstance instead.
    private static FetcherCallsHandler instance;
    private static Context context;

    @NonNull
    private static FetcherCallsHandler getInstance(){
        if(context == null){
            throw new IllegalStateException("Context is null. Call init() with valid context");
        }

        if(instance == null){
            instance = new FetcherCallsHandler();
        }
        return instance;
    }

    public static void init(@NonNull Context context){
        FetcherCallsHandler.context = context;
        loadSessions();
    }

    private FetcherCallsHandler(){
    }

    @Override
    public RepoServiceHandler get(Object key) {

        RepoServiceHandler handler = super.get(key);

        if(handler == null){
            handler = new RepoServiceFactory(context, this).create((int)key);
            if(handler != null) {
                put((int) key, handler);
            }
        }

        return handler;
    }

    public static void callListRepositories(@RepoServiceType int service, @NonNull ListRepositoriesRequest<?> request){
        IRepoServiceHandler handler = getInstance().get(service);
        makeCallIfThereIsNetwork(() -> handler.callListRepositories(request), request.getUiServiceResponse());
    }

    public static void callExchangeToken(@NonNull ExchangeTokenRequest<GitHubAccessToken> request){
        RepoServiceHandler handler = getInstance().get(FetcherCallsHandler.GITHUB);
        request.addServiceResponse(RxJavaController.IO, new RepoServiceResponse<GitHubAccessToken>() {
            @Override
            public void onSuccess(GitHubAccessToken object) {
                handler.setOAuthToken(object.getAccessToken());
            }

            @Override
            public void onError(Throwable t) {

            }
        });

        makeCallIfThereIsNetwork(() -> handler.exchangeToken(request), request.getUiServiceResponse());
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

    private static void makeCallIfThereIsNetwork(@NonNull Runnable runnable, @Nullable RepoServiceResponse<?> callback){
        if(ServiceUtils.isNetworkAvailable(context)){
            runnable.run();
        } else if(callback != null){
            SubscriberAdapter<?> subscriberAdapter = new SubscriberAdapter<>(callback);
            subscriberAdapter.onError(new NetworkErrorException());
        }
    }

    public static void unSubscribe(Fragment fragment){
        int id = System.identityHashCode(fragment);
        for (RepoServiceHandler serviceHandler: getInstance().values())
        {
            serviceHandler.removeSubscribers(id);
        }
    }

    @Override
    public void onTokenChanged(OAuthClientService oAuthClientService) {
        if(oAuthClientService instanceof GitHubServiceHandler) {
            new TokenSharedPrefs(context).saveToken(TokenSharedPrefs.GITHUB.getName(), oAuthClientService.getOAuthToken());
        } else if(oAuthClientService instanceof BitBucketServiceHandler){
            new TokenSharedPrefs(context).saveToken(TokenSharedPrefs.BITBUCKET.getName(), oAuthClientService.getOAuthToken());
        }

    }

    private static void loadSessions(){
        Map<Class, String> map = new TokenSharedPrefs(context).getTokens();
        if(map != null){
            for(Map.Entry<Class, String> entry : map.entrySet()){
                if(entry.getKey() == TokenSharedPrefs.GITHUB){
                    getInstance().get(GITHUB).setOAuthToken(entry.getValue());
                } else if(entry.getKey() == TokenSharedPrefs.BITBUCKET){
                    getInstance().get(BITBUCKET).setOAuthToken(entry.getValue());
                }
            }
        }
    }
}
