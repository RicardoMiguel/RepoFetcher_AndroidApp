package com.model.bitbucket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.model.ExpirableAccessToken;

/**
 * Created by ricar on 16/09/2016.
 */
public class BitBucketAccessToken implements ExpirableAccessToken{

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("scopes")
    @Expose
    private String scopes;
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("token_type")
    @Expose
    private String tokenType;

    /**
     *
     * @return
     * The accessToken
     */
    public String getToken() {
        return accessToken;
    }

    /**
     *
     * @param accessToken
     * The access_token
     */
    public void setToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     *
     * @return
     * The scopes
     */
    public String getScopes() {
        return scopes;
    }

    /**
     *
     * @param scopes
     * The scopes
     */
    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    /**
     *
     * @return
     * The expiresIn
     */
    public int getExpiresIn() {
        return expiresIn != null ? expiresIn : 0;
    }

    @Override
    public String getRefreshCode() {
        return refreshToken;
    }

    /**
     *
     * @param expiresIn
     * The expires_in
     */
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     *
     * @param refreshToken
     * The refresh_token
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     *
     * @return
     * The tokenType
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     *
     * @param tokenType
     * The token_type
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

}
