package com.model;

/**
 * Created by ricar on 23/09/2016.
 */

public interface ExpirableAccessToken extends AccessToken {
    Integer getExpiresIn();
    String getRefreshCode();
}
