package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.service.Constants;
import com.service.RepoServiceResponse;

import java.util.Map;

public class GetOwnRepositoriesRequest<T> extends BaseRequest<T> {

    public GetOwnRepositoriesRequest(@NonNull Object context, @Nullable RepoServiceResponse<T> response) {
        super(context, response);
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = super.getParams();
        map.put(Constants.TYPE, Constants.ALL);
        return map;
    }
}
