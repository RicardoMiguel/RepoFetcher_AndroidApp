package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.service.RepoServiceResponse;

/**
 * Created by ricar on 16/09/2016.
 */
public class BaseBodyRequest<V, T> extends BaseRequest<T>{
    private V body;

    public BaseBodyRequest(@NonNull Object context, @NonNull V body, @Nullable RepoServiceResponse<T> response) {
        setHash(context);
        this.body = body;

        setUiServiceResponse(response);
    }

    public V getBody() {
        return body;
    }
}
