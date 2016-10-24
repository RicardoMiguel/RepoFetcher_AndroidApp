package com.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

import java.util.HashMap;
import java.util.Map;

import static com.service.holder.RepoServiceType.BITBUCKET;
import static com.service.holder.RepoServiceType.GITHUB;

/**
 * Created by ricar on 03/10/2016.
 */

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
        Map<Class, AccessToken> map = prefs.getTokens();
        if (map != null) {

            Map<Class, ExpirableAccessToken> expirablesMap = new HashMap<>();

            for (Map.Entry<Class, AccessToken> entry : map.entrySet()) {
                if(entry.getValue() instanceof ExpirableAccessToken){
                    ExpirableAccessToken expirableAccessToken = (ExpirableAccessToken) entry.getValue();
//                    expirableAccessToken.setExpiresIn(120);

                    if(OAuthUtils.calcDelay(expirableAccessToken) <= 0){
                        expirablesMap.put(entry.getKey(), expirableAccessToken);
                        map.remove(entry.getKey());
                    }
                }
            }

            loadTokens(map);

            Map<Class, Owner> ownerMap = prefs.getOwners();
            if (ownerMap != null) {
                loadOwners(ownerMap);
            }

            if(!expirablesMap.isEmpty()) {
                loadExpirableTokens(expirablesMap, request);
            } else {
                proceedCallback(request);
            }

        } else {
            proceedCallback(request);
        }

    }

    private void loadTokens(@NonNull Map<Class, AccessToken> map){
        for (Map.Entry<Class, AccessToken> entry : map.entrySet()) {
            AccessToken accessToken = entry.getValue();
            if (entry.getKey() == SessionSharedPrefs.GITHUB) {
                handlers.get(GITHUB).getOAuthClientManager().setAccessToken(accessToken);
            } else if (entry.getKey() == SessionSharedPrefs.BITBUCKET) {
                handlers.get(BITBUCKET).getOAuthClientManager().setAccessToken(accessToken);
            }
        }
    }

    private void loadOwners(@NonNull Map<Class, Owner> map){
        for (Map.Entry<Class, Owner> entry : map.entrySet()) {
            if (entry.getKey() == SessionSharedPrefs.GITHUB) {
                handlers.get(GITHUB).getOAuthClientManager().setOwner(entry.getValue());
            } else if (entry.getKey() == SessionSharedPrefs.BITBUCKET) {
                handlers.get(BITBUCKET).getOAuthClientManager().setOwner(entry.getValue());
            }
        }
    }

    private <S extends ExpirableAccessToken> void loadExpirableTokens(@NonNull Map<Class, S> map, @Nullable InitRequest initRequest) {
        counter = map.size();
        for (Map.Entry<Class,S> entry : map.entrySet()) {

            RepoServiceHandler repoServiceHandler = null;
            ExchangeTokenRequest<? extends ExpirableAccessToken> exchangeTokenRequest = null;
            int type = -1;
            if (entry.getKey() == SessionSharedPrefs.BITBUCKET) {

                type = BITBUCKET;
                repoServiceHandler = handlers.get(BITBUCKET);
                exchangeTokenRequest = new BitbucketRefreshTokenRequest(handlers,
                        entry.getValue().getRefreshCode(),
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
