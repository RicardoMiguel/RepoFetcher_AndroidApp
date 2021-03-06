package com.service.oauth;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.model.ExpirableAccessToken;
import com.model.Owner;

public class OAuthSessionManager implements OAuthClientManager{

    @Nullable private AccessToken accessToken;

    @Nullable protected Owner owner;
    @Nullable private Handler handler;
    private Runnable runnable;

    private OAuthClientRequester oAuthClientRequester;
    private int service;

    private OAuthSessionManager() {
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
            int delay = OAuthUtils.calcDelay(expirableAccessToken);
//                int delay = 10000;
            if(delay > 0) {
                getHandler().removeCallbacks(runnable);
                getHandler().postDelayed(runnable, delay);
            } else {
                getHandler().removeCallbacks(runnable);
                getHandler().post(runnable);
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
            getHandler().removeCallbacks(runnable);
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

    private Handler getHandler(){
        if(handler == null){
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }
}
