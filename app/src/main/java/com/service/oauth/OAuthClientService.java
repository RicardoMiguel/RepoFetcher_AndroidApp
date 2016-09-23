package com.service.oauth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.Owner;

/**
 * Created by ricar on 13/09/2016.
 */
public interface OAuthClientService {
    @NonNull String getClientId();
    @NonNull String getClientSecret();
    @NonNull String getAuthorizationUrl();
    @NonNull String getExchangeTokenUrl();
}
