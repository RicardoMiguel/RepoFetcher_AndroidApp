package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.service.RepoServiceResponse;

import java.util.Map;


/**
 * Created by ricar on 12/09/2016.
 */
public class ListRepositoriesRequest<T> extends BaseRequest<T> {

    private String user;

    public ListRepositoriesRequest(@NonNull Fragment context, @NonNull String user, @Nullable RepoServiceResponse<T> response) {
        setHash(System.identityHashCode(context));
        this.user = user;
        setServiceResponse(response);
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = super.getParams();
        map.put("type", "all");
        return map;
    }

    public String getUser() {
        return user;
    }

}
