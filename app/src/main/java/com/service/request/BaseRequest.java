package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.service.RepoServiceResponse;
import com.service.RxJavaController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricar on 12/09/2016.
 */
public abstract class BaseRequest<T> {

    private RepoServiceResponse<T> uiServiceResponse;
    @Nullable
    private Map<Integer,List<RepoServiceResponse<T>>> serviceResponseList;
    private int hash;
    private Map<String, String> params;

    BaseRequest(){

    }

    @Nullable
    public RepoServiceResponse<T> getUiServiceResponse() {
        return uiServiceResponse;
    }

    void setUiServiceResponse(@Nullable RepoServiceResponse<T> serviceResponse) {
        if(serviceResponse != null) {
            this.uiServiceResponse = serviceResponse;
            addServiceResponse(RxJavaController.MAIN_THREAD, serviceResponse);
        }
    }

    public void addServiceResponse(@RxJavaController.SchedulerType int type, @Nullable RepoServiceResponse<T> serviceResponse) {
        Map<Integer, List<RepoServiceResponse<T>>> serviceResponseList = getServiceResponseList();

        List<RepoServiceResponse<T>> list = serviceResponseList.get(type);

        if(list == null){
            list = new ArrayList<>();
        }

        list.add(serviceResponse);
        serviceResponseList.put(type, list);
    }

    @NonNull
    public Map<Integer, List<RepoServiceResponse<T>>> getServiceResponseList(){
        if(this.serviceResponseList == null){
            this.serviceResponseList = new HashMap<>();
        }
        return serviceResponseList;
    }

    public int getHash() {
        return hash;
    }

    void setHash(@NonNull Fragment fragment) {
        this.hash = System.identityHashCode(fragment);
    }

    public Map<String, String> getParams() {
        if(params == null){
            params = new HashMap<>();
        }
        return params;
    }

}
