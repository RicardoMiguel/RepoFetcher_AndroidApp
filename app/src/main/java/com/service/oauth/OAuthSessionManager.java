package com.service.oauth;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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
    private OAuthClientService oAuthClientService;

    public OAuthSessionManager() {
        runnable = this::refreshToken;
    }

    public OAuthSessionManager(@NonNull OAuthClientService oAuthClientService, @Nullable OAuthClientRequester oAuthClientRequester) {
        this();
        this.oAuthClientService = oAuthClientService;
        this.oAuthClientRequester = oAuthClientRequester;
    }

    public void setAccessToken(@Nullable AccessToken accessToken){

        if(accessToken instanceof ExpirableAccessToken){
            ExpirableAccessToken expirableAccessToken = (ExpirableAccessToken) accessToken;
            if(expirableAccessToken.getExpiresIn() != null && expirableAccessToken.getExpiresIn() > 0) {
//                int delay = (expirableAccessToken.getExpiresIn() * 1000) - 120000; // Milisconds - 2 minutes in milliseconds
                int delay = 10000;
                if(handler != null){
                    handler.removeCallbacks(runnable);
                }
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(runnable, delay);
            }
        }

        if(OAuthUtils.isTokenValid(accessToken)){
            this.accessToken = accessToken;
        } else if(this.accessToken != null){
            this.accessToken.setToken(null);
        }

        if(oAuthClientRequester != null){
            oAuthClientRequester.onTokenChanged(oAuthClientService, this);
        }
    }

    @Nullable
    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    private void refreshToken(){
        Log.d("olaolaolaola", "onRefreshToken");
        if(oAuthClientRequester != null) {
            oAuthClientRequester.onRefreshToken(oAuthClientService, this);
        }
    }

    @Override
    public void setOwner(@Nullable Owner owner) {
        this.owner = owner;
        if(oAuthClientRequester != null){
            oAuthClientRequester.onOwnerChanged(oAuthClientService, this);
        }
    }

    @Override
    @Nullable
    public Owner getOwner() {
        return owner;
    }
}
