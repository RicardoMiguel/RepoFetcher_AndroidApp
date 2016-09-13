package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.service.RepoServiceResponse;

import java.util.Map;

/**
 * Created by ricar on 13/09/2016.
 */
public class ExchangeTokenRequest<T> extends BaseRequest<T>{

    private String code;

    public ExchangeTokenRequest(@NonNull Fragment context, @NonNull String code, @Nullable RepoServiceResponse<T> response) {
        setHash(context);
        setServiceResponse(response);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = super.getParams();
        map.put("client_id", "6ad10b7063dcf95b3eaa");
        map.put("client_secret", "7d511821d6c8a50e1cb44f34926eb3cc613d7273");
        map.put("code", code);
        return map;
    }
}
