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
}
