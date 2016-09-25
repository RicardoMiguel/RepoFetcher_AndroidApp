package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.model.bitbucket.BitBucketAccessToken;
import com.service.Constants;
import com.service.RepoServiceResponse;

/**
 * Created by ricar on 23/09/2016.
 */

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
