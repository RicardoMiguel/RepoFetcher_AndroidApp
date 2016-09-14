package com.service.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.service.Constants;
import com.service.OAuthClientService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ricar on 14/09/2016.
 */
public class OAuthInterceptor implements Interceptor {
    private OAuthClientService oAuthClientService;

    public OAuthInterceptor(@NonNull OAuthClientService oAuthClientService) {
        this.oAuthClientService = oAuthClientService;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain
                .request()
                .newBuilder();

        if(!TextUtils.isEmpty(oAuthClientService.getOAuthToken())){
                    request.addHeader(Constants.AUTHORIZATION, Constants.TOKEN + " " + oAuthClientService.getOAuthToken());
        }
        return chain.proceed(request.build());
    }
}
