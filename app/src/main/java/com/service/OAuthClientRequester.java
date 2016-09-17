package com.service;

/**
 * Created by ricar on 15/09/2016.
 */
public interface OAuthClientRequester {
    void onTokenChanged(OAuthClientService oAuthClientService);
    void onOwnerChanged(OAuthClientService oAuthClientService);
}
