package com.service.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.model.AccessToken;
import com.service.Constants;
import com.service.oauth.OAuthSessionManager;
import com.service.oauth.OAuthUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ricar on 14/09/2016.
 */
public class OAuthInterceptor implements Interceptor {
    private OAuthSessionManager oAuthClientService;

    public OAuthInterceptor(@NonNull OAuthSessionManager oAuthClientService) {
        this.oAuthClientService = oAuthClientService;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain
                .request()
                .newBuilder();
        AccessToken token = oAuthClientService.getAccessToken();
        if(OAuthUtils.isTokenValid(token)) {
            request.addHeader(Constants.AUTHORIZATION, Constants.BEARER + " " + token.getToken());
        }
        Response response = chain.proceed(request.build());

        if(response.code() == 401){
            oAuthClientService.setAccessToken(null);
        }

        return response;
    }
}
