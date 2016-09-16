package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.model.github.GitHubAccessToken;
import com.service.Constants;
import com.service.RepoServiceResponse;

import java.util.Map;

/**
 * Created by ricar on 13/09/2016.
 */
public class ExchangeTokenRequest<T> extends BaseRequest<T> implements IExchangeToken{

    protected String code;
    protected String clientId;
    protected String clientSecret;

    public ExchangeTokenRequest(@NonNull Fragment context,
                                @NonNull String code,
                                @NonNull String clientId,
                                @NonNull String clientSecret,
                                @Nullable RepoServiceResponse<T> response) {
        setHash(context);
        setUiServiceResponse(response);
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
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
