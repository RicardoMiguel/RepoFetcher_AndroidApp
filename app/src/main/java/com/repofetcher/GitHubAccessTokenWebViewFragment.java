package com.repofetcher;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.model.github.GitHubAccessToken;
import com.service.Constants;
import com.service.FetcherCallsHandler;
import com.service.RepoServiceResponse;
import com.service.holder.RepoServiceType;
import com.service.request.GitHubExchangeTokenRequest;

/**
 * Created by ricar on 16/09/2016.
 */
public class GitHubAccessTokenWebViewFragment extends AccessTokenWebViewFragment{

    @Override
    protected String getQuery() {
        return Uri.parse(authorizationUrl)
                .buildUpon()
                .appendQueryParameter(Constants.CLIENT_ID, clientId)
                .appendQueryParameter(Constants.SCOPE, Constants.REPO)
                .build().toString();
    }

    protected void exchangeCodeForToken(@NonNull String code){
        FetcherCallsHandler.callExchangeToken(getType(), new GitHubExchangeTokenRequest(this, code, clientId, clientSecret, new RepoServiceResponse<GitHubAccessToken>() {
            @Override
            public void onSuccess(GitHubAccessToken object) {
                goBack();
            }

            @Override
            public void onError(Throwable t) {
            }
        }));
    }

    @Override
    protected int getType() {
        return RepoServiceType.GITHUB;
    }
}
