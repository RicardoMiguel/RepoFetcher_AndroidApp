package com.service.oauth;

import android.support.annotation.NonNull;

public interface OAuthClientService {
    @NonNull String getClientId();
    @NonNull String getClientSecret();
    @NonNull String getAuthorizationUrl();
    @NonNull String getExchangeTokenUrl();
}
