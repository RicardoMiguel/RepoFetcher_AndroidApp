package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.bitbucket.BitBucketAccessToken;
import com.service.Constants;
import com.service.RepoServiceResponse;

public class BitbucketRefreshTokenRequest extends BitbucketExchangeTokenRequest{
    public BitbucketRefreshTokenRequest(@NonNull Object context,
                                        @NonNull String refreshCode,
                                        @NonNull String clientId,
                                        @NonNull String clientSecret,
                                        @Nullable RepoServiceResponse<BitBucketAccessToken> response) {
        super(context, refreshCode, clientId, clientSecret, response);
    }

    @Override
    public String getAuthorizationGrant() {
        return Constants.REFRESH_TOKEN;
    }
}
