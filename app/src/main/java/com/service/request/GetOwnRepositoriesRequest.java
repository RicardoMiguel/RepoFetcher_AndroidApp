package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.service.Constants;
import com.service.RepoServiceResponse;

import java.util.Map;

/**
 * Created by ricar on 15/09/2016.
 */
public class GetOwnRepositoriesRequest<T> extends BaseRequest<T> {

    public GetOwnRepositoriesRequest(@NonNull Fragment context, @Nullable RepoServiceResponse<T> response) {
        setHash(context);
        setUiServiceResponse(response);
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = super.getParams();
        map.put(Constants.TYPE, Constants.ALL);
        return map;
    }
}
