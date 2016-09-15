package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.service.Constants;
import com.service.RepoServiceResponse;

import java.util.Map;


/**
 * Created by ricar on 12/09/2016.
 */
public class ListRepositoriesRequest<T> extends ListOwnRepositoriesRequest<T> {

    private String user;

    public ListRepositoriesRequest(@NonNull Fragment context, @NonNull String user, @Nullable RepoServiceResponse<T> response) {
        super(context,response);
        this.user = user;
    }

    public String getUser() {
        return user;
    }

}
