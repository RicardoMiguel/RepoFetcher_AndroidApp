package com.service;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.model.AccessToken;
import com.model.Owner;
import com.model.bitbucket.BitBucketAccessToken;
import com.model.bitbucket.BitBucketOwner;
import com.service.request.BitbucketExchangeTokenRequest;
import com.service.request.ExchangeTokenRequest;
import com.service.request.GetOwnRepositoriesRequest;
import com.service.request.GetOwnerRequest;
import com.service.request.GetRepositoriesRequest;
import com.service.request.RedefineUiCallbackVisitor;
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

    public static void callListRepositories(@RepoServiceType int service, @NonNull GetRepositoriesRequest<?> request){
        IRepoServiceHandler handler = getInstance().get(service);
        makeCallIfThereIsNetwork(() -> handler.callListRepositories(request), request.getUiServiceResponse());
    }

    public static void callListRepositories(@RepoServiceType int service, @NonNull GetOwnRepositoriesRequest<?> request){
        IRepoServiceHandler handler = getInstance().get(service);
        makeCallIfThereIsNetwork(() -> handler.callListRepositories(request), request.getUiServiceResponse());
    }

    public static <S extends AccessToken> void callExchangeToken(@RepoServiceType int service, @NonNull ExchangeTokenRequest<S> request) {
        RepoServiceHandler handler = getInstance().get(service);
        request.addServiceResponse(RxJavaController.IO, new RepoServiceResponse<S>() {
            @Override
            public void onSuccess(S object) {
                handler.setOAuthToken(object.getAccessToken());
            }

            @Override
            public void onError(Throwable t) {

            }
        });

        if(service == BITBUCKET){
            callGetOwnerBitbucketCase((BitbucketExchangeTokenRequest) request);
        }

        makeCallIfThereIsNetwork(() -> handler.exchangeToken(request), request.getUiServiceResponse());

    }

    private static void callGetOwnerBitbucketCase(@NonNull BitbucketExchangeTokenRequest request){
        RepoServiceResponse<BitBucketAccessToken> oldResponse = request.getUiServiceResponse();
        RepoServiceResponse<BitBucketAccessToken> newResponse = new RepoServiceResponse<BitBucketAccessToken>() {
            @Override
            public void onSuccess(BitBucketAccessToken accessToken) {
                callGetOwner(BITBUCKET, new GetOwnerRequest<>(request.getHash(), new RepoServiceResponse<BitBucketOwner>() {
                    @Override
                    public void onSuccess(BitBucketOwner owner) {
                        if(oldResponse != null) {
                            oldResponse.onSuccess(accessToken);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if(oldResponse != null) {
                            oldResponse.onError(t);
                        }
                    }
                }));
            }

            @Override
            public void onError(Throwable t) {
                if(oldResponse != null) {
                    oldResponse.onError(t);
                }
            }
        };

        new RedefineUiCallbackVisitor().redefine( request, newResponse);
    }

    public static <S extends Owner> void callGetOwner(@RepoServiceType int service, @NonNull GetOwnerRequest<S> request){
        RepoServiceHandler handler = getInstance().get(service);
        request.addServiceResponse(RxJavaController.MAIN_THREAD, new RepoServiceResponse<S>() {
            @Override
            public void onSuccess(S object) {
                handler.setOwner(object);
            }

            @Override
            public void onError(Throwable t) {

            }
        });
        makeCallIfThereIsNetwork(() -> handler.callGetOwner(request), request.getUiServiceResponse());
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
            new SessionSharedPrefs(context).saveToken(SessionSharedPrefs.GITHUB.getName(), oAuthClientService.getOAuthToken());
        } else if(oAuthClientService instanceof BitBucketServiceHandler){
            new SessionSharedPrefs(context).saveToken(SessionSharedPrefs.BITBUCKET.getName(), oAuthClientService.getOAuthToken());
        }

    }

    private static void loadSessions(){
        Map<Class, String> map = new SessionSharedPrefs(context).getTokens();
        if(map != null){
            for(Map.Entry<Class, String> entry : map.entrySet()){
                if(entry.getKey() == SessionSharedPrefs.GITHUB){
                    getInstance().get(GITHUB).setOAuthToken(entry.getValue());
                } else if(entry.getKey() == SessionSharedPrefs.BITBUCKET){
                    getInstance().get(BITBUCKET).setOAuthToken(entry.getValue());
                }
            }
        }
    }

    public static boolean hasSessions(){
        boolean tokenFound = false;
        for(RepoServiceHandler repoServiceHandler : getInstance().values()){
            if(!TextUtils.isEmpty(repoServiceHandler.getOAuthToken())){
                tokenFound = true;
            }
        }
        return tokenFound;
    }

    public static boolean hasSession(@RepoServiceType int service){
        return !TextUtils.isEmpty(getInstance().get(service).getOAuthToken());
    }
}
