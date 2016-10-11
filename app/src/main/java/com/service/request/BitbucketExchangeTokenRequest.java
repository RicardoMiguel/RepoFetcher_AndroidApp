package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.bitbucket.BitBucketAccessToken;
import com.service.Constants;
import com.service.RepoServiceResponse;
import com.service.ServiceUtils;

import java.util.Map;

/**
 * Created by ricar on 16/09/2016.
 */
public class BitbucketExchangeTokenRequest extends ExchangeTokenRequest<BitBucketAccessToken>{

    public BitbucketExchangeTokenRequest(@NonNull Object context,
                                         @NonNull String code,
                                         @NonNull String clientId,
                                         @NonNull String clientSecret,
                                         @Nullable RepoServiceResponse<BitBucketAccessToken> response) {
        super(context, code, clientId, clientSecret, response);
    }

    public String getBasicAuthorization(){
        return ServiceUtils.getBasicAuthorization(clientId, clientSecret);
    }

    @Nullable
    @Override
    public Map<String, String> getParams() {
        return null;
    }

    public String getAuthorizationGrant(){
        return Constants.AUTHORIZATION_CODE;
    }

    public String getCode(){
        return code;
    }
}
