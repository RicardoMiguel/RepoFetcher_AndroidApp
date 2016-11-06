package com.service.interceptor;

import android.support.annotation.NonNull;

import com.model.AccessToken;
import com.service.Constants;
import com.service.oauth.OAuthClientManager;
import com.service.oauth.OAuthUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OAuthInterceptor implements Interceptor {
    private OAuthClientManager oAuthClientService;

    public OAuthInterceptor(@NonNull OAuthClientManager oAuthClientService) {
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
