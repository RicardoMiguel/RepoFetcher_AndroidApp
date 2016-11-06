package com.service.oauth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.model.Owner;

public interface OAuthClientRequester {
    void onTokenChanged(int service, @Nullable AccessToken accessToken);
    void onOwnerChanged(int service, @Nullable Owner owner);
    void onRefreshToken(int service, @NonNull String refresh);
}
