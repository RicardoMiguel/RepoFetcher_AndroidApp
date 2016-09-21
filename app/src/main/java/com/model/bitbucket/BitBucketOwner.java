package com.model.bitbucket;

/**
 * Created by ricar on 07/09/2016.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.model.Owner;

public class BitBucketOwner implements Owner {

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

    public BitBucketOwner() {
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

    @Override
    public void setLogin(String login) {
        this.username = login;
    }


    protected BitBucketOwner(Parcel in) {
        username = in.readString();
        displayName = in.readString();
        type = in.readString();
        uuid = in.readString();
        links = (Links) in.readValue(Links.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(displayName);
        dest.writeString(type);
        dest.writeString(uuid);
        dest.writeValue(links);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BitBucketOwner> CREATOR = new Parcelable.Creator<BitBucketOwner>() {
        @Override
        public BitBucketOwner createFromParcel(Parcel in) {
            return new BitBucketOwner(in);
        }

        @Override
        public BitBucketOwner[] newArray(int size) {
            return new BitBucketOwner[size];
        }
    };
}
