package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.service.RepoServiceResponse;
import com.service.rx.RxJavaController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricar on 12/09/2016.
 */
public abstract class BaseRequest<T> {

    private RepoServiceResponse<T> uiServiceResponse;
    @Nullable
    private ServiceResponseMapAdapter<T> responseMap;
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

    public void addServiceResponse(@RxJavaController.SchedulerType int type, @NonNull RepoServiceResponse<T> serviceResponse){
        getServiceResponseList().add(type, serviceResponse);
    }

    @NonNull
    public ServiceResponseMapAdapter<T> getServiceResponseList(){
        if(this.responseMap == null){
            this.responseMap = new ServiceResponseMapAdapter<>();
        }
        return responseMap;
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
