package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.service.RepoServiceResponse;
import com.service.ServiceUtils;
import com.service.rx.RxJavaController;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRequest<T> {

    private RepoServiceResponse<T> uiServiceResponse;
    @Nullable
    private ServiceResponseMapAdapter<T> responseMap;
    private int hash;
    private Map<String, String> params;

    BaseRequest(){

    }

    BaseRequest(@NonNull Object context, @Nullable RepoServiceResponse<T> uiServiceResponse) {
        setUiServiceResponse(uiServiceResponse);
        setHash(context);
    }

    BaseRequest(@NonNull BaseRequest<T> baseRequest){
        this.uiServiceResponse = baseRequest.uiServiceResponse;
        this.responseMap = baseRequest.responseMap;
        this.hash = baseRequest.hash;
        this.params = baseRequest.params;
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

    void setHash(@NonNull Object fragment) {
        this.hash = ServiceUtils.getHashCode(fragment);
    }

    void setHash(int hash){
        this.hash = hash;
    }

    public Map<String, String> getParams() {
        if(params == null){
            params = new HashMap<>();
        }
        return params;
    }

}
