package com.model.bitbucket;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.model.Owner;
import com.model.Repo;

public class BitBucketRepo implements Repo {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("full_name")
    @Expose
    private String fullName;

    @SerializedName("owner")
    @Expose
    private BitBucketOwner owner;

    @SerializedName("description")
    @Expose
    private String description;

    public BitBucketRepo() {
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The createdOn
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     *
     * @param createdOn
     * The created_on
     */
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    /**
     *
     * @return
     * The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullName
     * The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     *
     * @return
     * The owner
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    protected BitBucketRepo(Parcel in) {
        name = in.readString();
        createdOn = in.readString();
        fullName = in.readString();
        owner = (BitBucketOwner) in.readValue(BitBucketOwner.class.getClassLoader());
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(createdOn);
        dest.writeString(fullName);
        dest.writeValue(owner);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BitBucketRepo> CREATOR = new Parcelable.Creator<BitBucketRepo>() {
        @Override
        public BitBucketRepo createFromParcel(Parcel in) {
            return new BitBucketRepo(in);
        }

        @Override
        public BitBucketRepo[] newArray(int size) {
            return new BitBucketRepo[size];
        }
    };
}