package com.service.oauth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.model.AccessToken;
import com.model.ExpirableAccessToken;

public final class OAuthUtils {

    private OAuthUtils(){}

    public static final int SECURE_RAGE_FOR_DELAY = 120;

    public static boolean isTokenValid(@Nullable AccessToken accessToken){
        return accessToken != null && !TextUtils.isEmpty(accessToken.getToken());
    }

    public static int calcTimeToRefreshToken(long dateAcquired, int expiration){
        Long expiresIn = -(System.currentTimeMillis() - (dateAcquired + secondsToMilliseconds(expiration)));
        return millisecondsToSeconds(expiresIn.intValue());
    }

    public static int secondsToMilliseconds(int seconds){
        return seconds * 1000;
    }

    public static int millisecondsToSeconds(int milliseconds){
        return milliseconds / 1000;
    }

    public static int calcDelay(@NonNull ExpirableAccessToken expirableAccessToken){
        return secondsToMilliseconds(expirableAccessToken.getExpiresIn()) - secondsToMilliseconds(SECURE_RAGE_FOR_DELAY); // time - 2 minutes
    }
}
