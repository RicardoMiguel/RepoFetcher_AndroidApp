package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.service.RepoServiceResponse;

public class BaseBodyRequest<V, T> extends BaseRequest<T>{
    private V body;

    public BaseBodyRequest(@NonNull Object context, @NonNull V body, @Nullable RepoServiceResponse<T> response) {
        super(context, response);
        this.body = body;
    }

    public V getBody() {
        return body;
    }
}
