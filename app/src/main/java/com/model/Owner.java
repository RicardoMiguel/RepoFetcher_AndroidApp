package com.model;

import android.os.Parcelable;

/**
 * Created by ricar on 07/09/2016.
 */
public interface Owner extends Parcelable{
    String getLogin();
    String getAvatarUrl();
    void setLogin(String login);
}
