package com.controller;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.controller.ErrorType.NETWORK_ERROR;
import static com.controller.ErrorType.UNEXPECTED_ERROR;

/**
 * Created by ricar on 31/10/2016.
 */
@IntDef({NETWORK_ERROR, UNEXPECTED_ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface ErrorType {

    int NETWORK_ERROR = 0;
    int UNEXPECTED_ERROR = 1;
}
