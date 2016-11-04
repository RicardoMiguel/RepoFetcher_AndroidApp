package com.service;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.model.ExpirableAccessToken;
import com.model.Owner;
import com.model.bitbucket.BitBucketOwner;
import com.service.handler.RepoServiceFactory;
import com.service.handler.RepoServiceHandler;
import com.service.holder.RepoServiceType;
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

import java.util.ArrayList;
import java.util.HashMap;

import static com.service.holder.RepoServiceType.BITBUCKET;
import static com.service.holder.RepoServiceType.GITHUB;

public class FetcherCallsHandler extends HashMap<Integer, RepoServiceHandler> implements OAuthClientRequester {

    //The instance might be null. Use getInstance instead.
    @Nullable private static FetcherCallsHandler instance;

    @NonNull
    private static FetcherCallsHandler getInstance(){
        if(instance == null){
            instance = new FetcherCallsHandler();
        }
        return instance;
    }

    public static void load(@Nullable InitRequest request){
        new Initializer(ServiceUtils.getContext(), getInstance()).loadSessions(request);
    }

    private FetcherCallsHandler(){
    }

    @Override
    public RepoServiceHandler get(Object key) {

        RepoServiceHandler handler = super.get(key);

        if(handler == null){
            handler = new RepoServiceFactory(ServiceUtils.getContext(), new OAuthSessionManager((int)key, this)).create((int)key);
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
        if(ServiceUtils.isNetworkAvailable(ServiceUtils.getContext())){
            runnable.run();
        } else if(callback != null){
            callback.onError(new NetworkErrorException());
        }
    }

    public static void unSubscribe(Object fragment){
        int id = ServiceUtils.getHashCode(fragment);
        //TODO re-do to unsubscribe only when it's necessary
        int ownId = ServiceUtils.getHashCode(getInstance());
        for (RepoServiceHandler serviceHandler: getInstance().values()) {
            serviceHandler.removeSubscribers(id);
            serviceHandler.removeSubscribers(ownId);
        }
    }

    public static void removeToken(@RepoServiceType int service){
        getInstance().get(service).getOAuthClientManager().setAccessToken(null);
    }

    @Override
    public void onTokenChanged(int service, @Nullable AccessToken accessToken) {
        new SessionSharedPrefs(ServiceUtils.getContext()).saveToken(service, accessToken);
    }

    @Override
    public void onOwnerChanged(int service, @Nullable Owner owner) {
        if(owner != null) {
            new SessionSharedPrefs(ServiceUtils.getContext()).saveOwner(service, owner);
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

    public static @RepoServiceType int[] getServicesAlias(){
        return new int[]{GITHUB, BITBUCKET};
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
