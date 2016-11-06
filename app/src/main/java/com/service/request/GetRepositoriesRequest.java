package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.service.RepoServiceResponse;

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
