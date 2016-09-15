package com.service;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.model.github.GitHubAccessToken;
import com.repofetcher.RepoFetcherApplication;
import com.service.request.ExchangeTokenRequest;
import com.service.request.ListRepositoriesRequest;
import com.service.rx.RxJavaController;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

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
            handler = new RepoServiceFactory(RepoFetcherApplication.getContext(), this).create((int)key);
            if(handler != null) {
                put((int) key, handler);
            }
        }

        return handler;
    }

    @Override
    public void onTokenChanged(OAuthClientService oAuthClientService) {
        if(oAuthClientService instanceof GitHubServiceHandler) {
            saveToken(GitHubServiceHandler.class.getName(), oAuthClientService.getOAuthToken());
        } else if(oAuthClientService instanceof BitBucketServiceHandler){
            saveToken(BitBucketServiceHandler.class.getName(), oAuthClientService.getOAuthToken());
        }

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
        if(ServiceUtils.isNetworkAvailable(RepoFetcherApplication.getContext())){
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

    private static void saveToken(String file, String token){
        SharedPreferences sharedPref = RepoFetcherApplication.getContext().getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.TOKEN, token);
        editor.commit();
    }

    private static boolean hasToken(){
        return !TextUtils.isEmpty(RepoFetcherApplication.getContext().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE).getString(Constants.TOKEN,null));
    }
}
