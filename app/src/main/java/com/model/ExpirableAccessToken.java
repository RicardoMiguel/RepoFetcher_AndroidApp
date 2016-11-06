package com.model;

public interface ExpirableAccessToken extends AccessToken {
    int getExpiresIn();
    void setExpiresIn(int expiresIn);
    String getRefreshCode();
    void setRefreshToken(String refreshToken);
}
