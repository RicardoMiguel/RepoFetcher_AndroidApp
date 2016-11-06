package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.github.GitHubAccessToken;
import com.service.Constants;
import com.service.RepoServiceResponse;

import java.util.Map;

public class GitHubExchangeTokenRequest extends ExchangeTokenRequest<GitHubAccessToken> {

    public GitHubExchangeTokenRequest(@NonNull Object context,
                                      @NonNull String code,
                                      @NonNull String clientId,
                                      @NonNull String clientSecret,
                                      @Nullable RepoServiceResponse<GitHubAccessToken> response) {
        super(context, code, clientId, clientSecret, response);
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = super.getParams();
        map.put(Constants.CLIENT_ID, clientId);
        map.put(Constants.CLIENT_SECRET, clientSecret);
        map.put(Constants.CODE, code);
        return map;
    }
}
