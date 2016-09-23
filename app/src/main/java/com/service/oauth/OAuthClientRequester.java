package com.service.oauth;

/**
 * Created by ricar on 15/09/2016.
 */
public interface OAuthClientRequester {
    void onTokenChanged(OAuthClientService oAuthClientService, OAuthClientManager oAuthClientManager);
    void onOwnerChanged(OAuthClientService oAuthClientService, OAuthClientManager oAuthClientManager);
    void onRefreshToken(OAuthClientService oAuthClientService, OAuthClientManager oAuthClientManager);
}
