package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.service.RepoServiceResponse;

public class ExchangeTokenRequest<T extends AccessToken> extends BaseRequest<T>{

    protected String code;
    protected String clientId;
    protected String clientSecret;

    public ExchangeTokenRequest(@NonNull Object context,
                                @NonNull String code,
                                @NonNull String clientId,
                                @NonNull String clientSecret,
                                @Nullable RepoServiceResponse<T> response) {
        super(context, response);
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
