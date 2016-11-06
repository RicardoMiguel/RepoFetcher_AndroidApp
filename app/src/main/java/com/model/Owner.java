package com.model;

import android.os.Parcelable;

public interface Owner extends Parcelable{
    String getLogin();
    String getAvatarUrl();
    void setLogin(String login);
}
