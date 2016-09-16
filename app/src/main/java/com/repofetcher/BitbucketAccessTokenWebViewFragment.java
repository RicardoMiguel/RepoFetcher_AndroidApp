package com.repofetcher;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.service.Constants;
import com.service.FetcherCallsHandler;

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

    }

    @Override
    protected int getType() {
        return FetcherCallsHandler.BITBUCKET;
    }
}
