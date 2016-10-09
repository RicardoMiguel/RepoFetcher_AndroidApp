package com.model;

/**
 * Created by ricar on 23/09/2016.
 */

public interface ExpirableAccessToken extends AccessToken {
    int getExpiresIn();
    void setExpiresIn(int expiresIn);
    String getRefreshCode();
    void setRefreshToken(String refreshToken);
}
