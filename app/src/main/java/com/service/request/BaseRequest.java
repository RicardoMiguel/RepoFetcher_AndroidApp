package com.service.request;

import android.support.annotation.Nullable;

import com.service.RepoServiceResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricar on 12/09/2016.
 */
public abstract class BaseRequest<T> {
    private RepoServiceResponse<T> serviceResponse;
    private int hash;
    private Map<String, String> params;

    BaseRequest(){
    }

    public RepoServiceResponse<T> getServiceResponse() {
        return serviceResponse;
    }

    void setServiceResponse(@Nullable RepoServiceResponse<T> serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    public int getHash() {
        return hash;
    }

    void setHash(int hash) {
        this.hash = hash;
    }

    public Map<String, String> getParams() {
        return params = new HashMap<>();
    }

}
