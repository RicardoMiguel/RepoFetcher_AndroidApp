package com.service.oauth;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.model.ExpirableAccessToken;
import com.model.Owner;

/**
 * Created by ricar on 23/09/2016.
 */

public class OAuthSessionManager implements OAuthClientManager{

    @Nullable private AccessToken accessToken;

    @Nullable protected Owner owner;
    private Handler handler;
    private Runnable runnable;

    private OAuthClientRequester oAuthClientRequester;
    private int service;

    private OAuthSessionManager() {
        handler = new Handler(Looper.getMainLooper());
        runnable = this::refreshToken;
    }

    public OAuthSessionManager(int service, @Nullable OAuthClientRequester oAuthClientRequester) {
        this();
        this.service = service;
        this.oAuthClientRequester = oAuthClientRequester;
    }

    public void setAccessToken(@Nullable AccessToken accessToken){

        if(accessToken instanceof ExpirableAccessToken){
            ExpirableAccessToken expirableAccessToken = (ExpirableAccessToken) accessToken;
            if(expirableAccessToken.getExpiresIn() != null && expirableAccessToken.getExpiresIn() > 0) {
                int delay = (OAuthUtils.secondsToMilliseconds(expirableAccessToken.getExpiresIn())) - OAuthUtils.secondsToMilliseconds(120); // time - 2 minutes
//                int delay = 10000;

                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, delay);
            } else {
                handler.removeCallbacks(runnable);
                handler.post(runnable);
            }
        }

        if(OAuthUtils.isTokenValid(accessToken)){
            this.accessToken = accessToken;
        } else if(this.accessToken != null){
            this.accessToken.setToken(null);
        }

        if(oAuthClientRequester != null){
            oAuthClientRequester.onTokenChanged(service, accessToken);
        }
    }

    @Nullable
    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    private void refreshToken(){
        if(oAuthClientRequester != null && accessToken instanceof ExpirableAccessToken) {
            setAccessToken(null);
            oAuthClientRequester.onRefreshToken(service, ((ExpirableAccessToken) accessToken).getRefreshCode());
        }
    }

    @Override
    public void setOwner(@Nullable Owner owner) {
        this.owner = owner;
        if(oAuthClientRequester != null){
            oAuthClientRequester.onOwnerChanged(service, owner);
        }
    }

    @Override
    @Nullable
    public Owner getOwner() {
        return owner;
    }
}
