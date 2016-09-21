package com.model;

import android.os.Parcelable;

/**
 * Created by ricar on 07/09/2016.
 */
public interface Repo extends Parcelable{
    String getName();
    Owner getOwner();
}
