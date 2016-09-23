package com.model.github;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.model.AccessToken;

/**
 * Created by ricar on 13/09/2016.
 */
public class GitHubAccessToken implements AccessToken{

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("scope")
    @Expose
    private String scope;
    @SerializedName("token_type")
    @Expose
    private String tokenType;

    /**
     *
     * @return
     * The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    /**
     *
     * @return
     * The scope
     */
    public String getScope() {
        return scope;
    }

    /**
     *
     * @return
     * The tokenType
     */
    public String getTokenType() {
        return tokenType;
    }

}
