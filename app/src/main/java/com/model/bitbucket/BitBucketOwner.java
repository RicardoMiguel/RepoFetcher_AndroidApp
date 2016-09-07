package com.model.bitbucket;

/**
 * Created by ricar on 07/09/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.model.Owner;

public class BitBucketOwner implements Owner{

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("links")
    @Expose
    private Links links;

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @param displayName
     * The display_name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     *
     * @param uuid
     * The uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getLogin() {
        return username;
    }

    @Override
    public String getAvatarUrl() {
        return links.avatar.href;
    }

    private class Links {

        @SerializedName("avatar")
        @Expose
        Avatar avatar;
    }

    private class Avatar{

        @SerializedName("href")
        @Expose
        String href;


    }
}
