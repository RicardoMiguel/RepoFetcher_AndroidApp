package com.model.github;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.model.Owner;
import com.model.Repo;

public class GitHubRepo implements Repo {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("owner")
    @Expose
    private GitHubOwner owner;

    public GitHubRepo() {
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
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
     * @param owner
     * The owner
     */
    public void setOwner(GitHubOwner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return id+" "+ name;
    }

    protected GitHubRepo(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        fullName = in.readString();
        owner = (GitHubOwner) in.readValue(GitHubOwner.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(fullName);
        dest.writeValue(owner);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GitHubRepo> CREATOR = new Parcelable.Creator<GitHubRepo>() {
        @Override
        public GitHubRepo createFromParcel(Parcel in) {
            return new GitHubRepo(in);
        }

        @Override
        public GitHubRepo[] newArray(int size) {
            return new GitHubRepo[size];
        }
    };
}