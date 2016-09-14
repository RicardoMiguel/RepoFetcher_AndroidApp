package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.service.RepoServiceResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricar on 12/09/2016.
 */
public abstract class BaseRequest<T> {
    private RepoServiceResponse<T> uiServiceResponse;
    private List<RepoServiceResponse<T>> serviceResponseList;
    private int hash;
    private Map<String, String> params;

    BaseRequest(){

    }

    @Nullable
    public RepoServiceResponse<T> getUiServiceResponse() {
        return uiServiceResponse;
    }

    void setUiServiceResponse(@Nullable RepoServiceResponse<T> serviceResponse) {
        this.uiServiceResponse = serviceResponse;
    }

    public void addServiceResponse(@Nullable RepoServiceResponse<T> serviceResponse) {
        if(this.serviceResponseList == null){
            this.serviceResponseList = new ArrayList<>();
        }
        serviceResponseList.add(serviceResponse);
    }

    public List<RepoServiceResponse<T>> getServiceResponseList(){
        return serviceResponseList;
    }
    public int getHash() {
        return hash;
    }

    void setHash(@NonNull Fragment fragment) {
        this.hash = System.identityHashCode(fragment);
    }

    public Map<String, String> getParams() {
        return params = new HashMap<>();
    }

}
