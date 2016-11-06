package com.model;

import android.os.Parcelable;

public interface Repo extends Parcelable{
    String getName();
    Owner getOwner();
}
