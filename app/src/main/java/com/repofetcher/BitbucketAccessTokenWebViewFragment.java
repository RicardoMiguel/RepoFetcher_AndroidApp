package com.repofetcher;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.model.bitbucket.BitBucketAccessToken;
import com.service.Constants;
import com.service.FetcherCallsHandler;
import com.service.RepoServiceResponse;
import com.service.holder.RepoServiceType;
import com.service.request.BitbucketExchangeTokenRequest;

/**
 * Created by ricar on 16/09/2016.
 */
public class BitbucketAccessTokenWebViewFragment extends AccessTokenWebViewFragment {

    @Override
    protected String getQuery() {
        return Uri.parse(authorizationUrl)
                .buildUpon()
                .appendQueryParameter(Constants.CLIENT_ID, clientId)
                .appendQueryParameter(Constants.RESPONSE_TYPE, Constants.CODE)
                .build().toString();
    }

    @Override
    protected void exchangeCodeForToken(@NonNull String code) {
        FetcherCallsHandler.callExchangeToken(getType(), new BitbucketExchangeTokenRequest(this,
                code,
                clientId,
                clientSecret,
                new RepoServiceResponse<BitBucketAccessToken>() {
                    @Override
                    public void onSuccess(BitBucketAccessToken object) {
                        goBack();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                }));
    }

    @Override
    protected int getType() {
        return RepoServiceType.BITBUCKET;
    }

    @Override
    protected boolean javaScriptEnable() {
        return true;
    }
}
