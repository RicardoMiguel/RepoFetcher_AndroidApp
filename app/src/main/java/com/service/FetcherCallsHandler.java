package com.service;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.model.ExpirableAccessToken;
import com.model.Owner;
import com.model.bitbucket.BitBucketOwner;
import com.service.handler.RepoServiceFactory;
import com.service.handler.RepoServiceHandler;
import com.service.oauth.OAuthClientRequester;
import com.service.oauth.OAuthSessionManager;
import com.service.oauth.OAuthUtils;
import com.service.oauth.SessionSharedPrefs;
import com.service.request.BitbucketRefreshTokenRequest;
import com.service.request.ExchangeTokenRequest;
import com.service.request.GetOwnRepositoriesRequest;
import com.service.request.GetOwnerRequest;
import com.service.request.GetRepositoriesRequest;
import com.service.request.InitRequest;
import com.service.request.RedefineUiCallbackVisitor;
import com.service.rx.RxJavaController;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ricar on 04/09/2016.
 */
public class FetcherCallsHandler extends HashMap<Integer, RepoServiceHandler> implements OAuthClientRequester {

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
    }

    public static void load(@Nullable InitRequest request){
        new InitController(context, getInstance()).loadSessions(request);
    }

    private FetcherCallsHandler(){
    }

    @Override
    public RepoServiceHandler get(Object key) {

        RepoServiceHandler handler = super.get(key);

        if(handler == null){
            handler = new RepoServiceFactory(context, new OAuthSessionManager((int)key, this)).create((int)key);
            if(handler != null) {
                put((int) key, handler);
            }
        }

        return handler;
    }

    public static void callListRepositories(@RepoServiceType int service, @NonNull GetRepositoriesRequest<?> request){
        RepoServiceHandler handler = getInstance().get(service);
        makeCallIfThereIsNetwork(() -> handler.callListRepositories(request), request.getUiServiceResponse());
    }

    public static void callListRepositories(@RepoServiceType int service, @NonNull GetOwnRepositoriesRequest<?> request){
        RepoServiceHandler handler = getInstance().get(service);
        makeCallIfThereIsNetwork(() -> handler.callListRepositories(request), request.getUiServiceResponse());
    }

    public static <S extends AccessToken> void callExchangeToken(@RepoServiceType int service, @NonNull ExchangeTokenRequest<S> request) {
        RepoServiceHandler handler = getInstance().get(service);
        request.addServiceResponse(RxJavaController.IO, new RepoServiceResponse<S>() {
            @Override
            public void onSuccess(S object) {
                handler.getOAuthClientManager().setAccessToken(object);
            }

            @Override
            public void onError(Throwable t) {

            }
        });

        if(service == BITBUCKET){
            callGetOwnerAfterTokenExchange(request);
        }

        makeCallIfThereIsNetwork(() -> handler.exchangeToken(request), request.getUiServiceResponse());

    }

    private static <S extends AccessToken> void callGetOwnerAfterTokenExchange(@NonNull ExchangeTokenRequest<S> request){
        RepoServiceResponse<S> oldResponse = request.getUiServiceResponse();
        RepoServiceResponse<S> newResponse = new RepoServiceResponse<S>() {
            @Override
            public void onSuccess(S accessToken) {
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

        new RedefineUiCallbackVisitor().swap(request, newResponse);
    }

    public static <S extends Owner> void callGetOwner(@RepoServiceType int service, @NonNull GetOwnerRequest<S> request){
        RepoServiceHandler handler = getInstance().get(service);
        request.addServiceResponse(RxJavaController.IO, new RepoServiceResponse<S>() {
            @Override
            public void onSuccess(S object) {
                handler.getOAuthClientManager().setOwner(object);
            }

            @Override
            public void onError(Throwable t) {

            }
        });
        makeCallIfThereIsNetwork(() -> handler.callGetOwner(request), request.getUiServiceResponse());
    }

    public static <S extends ExpirableAccessToken> void callRefreshToken(@RepoServiceType int type, ExchangeTokenRequest<S> request){
        RepoServiceHandler handler = getInstance().get(type);
        request.addServiceResponse(RxJavaController.IO, new RepoServiceResponse<S>() {
            @Override
            public void onSuccess(S object) {
                handler.getOAuthClientManager().setAccessToken(object);
            }

            @Override
            public void onError(Throwable t) {

            }
        });

        makeCallIfThereIsNetwork(() -> handler.refreshToken(request), request.getUiServiceResponse());
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

    public static void unSubscribe(Object fragment){
        int id = ServiceUtils.getHashCode(fragment);
        for (RepoServiceHandler serviceHandler: getInstance().values())
        {
            serviceHandler.removeSubscribers(id);
        }
    }

    @Override
    public void onTokenChanged(int service, @Nullable AccessToken accessToken) {
        if (service == GITHUB) {
            new SessionSharedPrefs(context).saveToken(SessionSharedPrefs.GITHUB.getName(), accessToken);
        } else if (service == BITBUCKET) {
            new SessionSharedPrefs(context).saveToken(SessionSharedPrefs.BITBUCKET.getName(), accessToken);
        }
    }

    @Override
    public void onOwnerChanged(int service, @Nullable Owner owner) {
        if(owner != null) {
            if (service == GITHUB) {
                new SessionSharedPrefs(context).saveOwner(SessionSharedPrefs.GITHUB.getName(), owner);
            } else if (service == BITBUCKET) {
                new SessionSharedPrefs(context).saveOwner(SessionSharedPrefs.BITBUCKET.getName(), owner);
            }
        }
    }

    @Override
    public void onRefreshToken(int service, @NonNull String refreshCode) {
        RepoServiceHandler oAuthClientService = get(service);
        if(service == BITBUCKET){
            callRefreshToken(BITBUCKET, new BitbucketRefreshTokenRequest(this,
                    refreshCode,
                    oAuthClientService.getClientId(),
                    oAuthClientService.getClientSecret(),
                    null));
        }

    }

    public static boolean hasSessions(){
        boolean tokenFound = false;
        for(RepoServiceHandler repoServiceHandler : getInstance().values()){
            tokenFound |= OAuthUtils.isTokenValid(repoServiceHandler.getOAuthClientManager().getAccessToken());
        }
        return tokenFound;
    }

    public static boolean hasSession(@RepoServiceType int service){
        return OAuthUtils.isTokenValid(getInstance().get(service).getOAuthClientManager().getAccessToken());
    }

    private static int[] getServicesAlias(){
        int[] classes = {GITHUB, BITBUCKET};
        return classes;
    }

    @NonNull
    public static ArrayList<Integer> getSessionsServicesAlias(){
        ArrayList<Integer> list = new ArrayList<>();
        int[] services = getServicesAlias();
        for(int alias: services){
            if(hasSession(alias)) {
                list.add(alias);
            }
        }
        return list;
    }
}
