package com.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.model.AccessToken;
import com.model.ExpirableAccessToken;
import com.model.Owner;
import com.model.bitbucket.BitBucketAccessToken;
import com.service.handler.RepoServiceHandler;
import com.service.oauth.OAuthUtils;
import com.service.oauth.SessionSharedPrefs;
import com.service.request.BitbucketRefreshTokenRequest;
import com.service.request.ExchangeTokenRequest;
import com.service.request.InitRequest;

import java.util.Map;

import static com.service.holder.RepoServiceType.BITBUCKET;

public class Initializer {

    private Context context;
    private Map<Integer, RepoServiceHandler> handlers;
    private int counter;

    public Initializer(@NonNull Context context, @NonNull Map<Integer, RepoServiceHandler> handlers) {
        this.context = context;
        this.handlers = handlers;
        this.counter = -1;
    }

    public void loadSessions(@Nullable InitRequest request) {
        SessionSharedPrefs prefs = new SessionSharedPrefs(context);
        SparseArray<AccessToken> map = prefs.getTokens(FetcherCallsHandler.getServicesAlias());

        if (map != null) {

            SparseArray<ExpirableAccessToken> expirablesMap = new SparseArray<>();

            for (int i = 0; i < map.size(); i++) {
                int key = map.keyAt(i);
                AccessToken value = map.get(key);
                if(value instanceof ExpirableAccessToken){
                    ExpirableAccessToken expirableAccessToken = (ExpirableAccessToken) value;
//                    expirableAccessToken.setExpiresIn(120);

                    if(OAuthUtils.calcDelay(expirableAccessToken) <= 0){
                        expirablesMap.put(key, expirableAccessToken);
                        //TODO: operation must be done previously
//                        map.remove(key);
                    }
                }
            }

//            for(int i = 0; i < expirablesMap.size() ; i++){
//                map.remove(expirablesMap.keyAt(i));
//            }

            loadTokens(map);

            SparseArray<Owner> ownerMap = prefs.getOwners(FetcherCallsHandler.getServicesAlias());
            if (ownerMap != null) {
                loadOwners(ownerMap);
            }

            if(expirablesMap.size() > 0) {
                loadExpirableTokens(expirablesMap, request);
            } else {
                proceedCallback(request);
            }

        } else {
            proceedCallback(request);
        }

    }

    private void loadTokens(@NonNull SparseArray<AccessToken> map){
        for (int i = 0; i < map.size() ; i++) {
            int key = map.keyAt(i);
            AccessToken value = map.get(key);
            handlers.get(key).getOAuthClientManager().setAccessToken(value);
        }
    }

    private void loadOwners(@NonNull SparseArray<Owner> map){
        for (int i = 0; i < map.size() ; i++) {
            int key = map.keyAt(i);
            Owner value = map.get(key);
            handlers.get(key).getOAuthClientManager().setOwner(value);
        }
    }

    private <S extends ExpirableAccessToken> void loadExpirableTokens(@NonNull SparseArray<S> map, @Nullable InitRequest initRequest) {
        counter = map.size();
        for (int i = 0; i < map.size() ; i++) {
            int key = map.keyAt(i);
            ExpirableAccessToken value = map.get(key);

            RepoServiceHandler repoServiceHandler = null;
            ExchangeTokenRequest<? extends ExpirableAccessToken> exchangeTokenRequest = null;
            int type = -1;
            if (key == BITBUCKET) {

                type = BITBUCKET;
                repoServiceHandler = handlers.get(BITBUCKET);
                exchangeTokenRequest = new BitbucketRefreshTokenRequest(handlers,
                        value.getRefreshCode(),
                        repoServiceHandler.getClientId(),
                        repoServiceHandler.getClientSecret(), new RepoServiceResponse<BitBucketAccessToken>() {
                    @Override
                    public void onSuccess(BitBucketAccessToken object) {
                        onRequestComplete(initRequest);
                    }

                    @Override
                    public void onError(Throwable t) {
                        onRequestComplete(initRequest);
                    }
                });

            }

            if(repoServiceHandler != null){
                FetcherCallsHandler.callRefreshToken(type, exchangeTokenRequest);
            }

        }

    }

    private void onRequestComplete(@Nullable InitRequest initRequest){
        if(--counter == 0){
            proceedCallback(initRequest);
        }
    }

    private void proceedCallback(@Nullable InitRequest initRequest){
        if(initRequest != null && initRequest.getUiServiceResponse() != null) {
            initRequest.getUiServiceResponse().onSuccess(null);
        }
    }
}
