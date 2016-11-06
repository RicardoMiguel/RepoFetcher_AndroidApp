package com.service.interceptor;

import com.service.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JsonInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain
                .request()
                .newBuilder()
                .addHeader(Constants.CONTENT_TYPE, Constants.APLLICATION_JSON)
                .addHeader(Constants.ACCEPT, Constants.APLLICATION_JSON)
                .build();
        return chain.proceed(request);
    }
}
