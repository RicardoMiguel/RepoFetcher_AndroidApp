package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.service.RepoServiceResponse;


/**
 * Created by ricar on 12/09/2016.
 */
public class GetRepositoriesRequest<T> extends GetOwnRepositoriesRequest<T> {

    private String user;

    public GetRepositoriesRequest(@NonNull Object context, @NonNull String user, @Nullable RepoServiceResponse<T> response) {
        super(context,response);
        this.user = user;
    }

    public String getUser() {
        return user;
    }

}
