package com.service.oauth;

import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.model.Owner;

/**
 * Created by ricar on 23/09/2016.
 */

public interface OAuthClientManager {

    void setOwner(@Nullable Owner owner);
    @Nullable Owner getOwner();

    void setAccessToken(@Nullable AccessToken token);
    @Nullable AccessToken getAccessToken();
}
