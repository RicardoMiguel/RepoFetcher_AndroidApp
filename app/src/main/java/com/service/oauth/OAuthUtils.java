package com.service.oauth;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.model.AccessToken;

/**
 * Created by ricar on 25/09/2016.
 */

public class OAuthUtils {
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
}
