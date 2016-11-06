package com.service.holder;

import android.support.annotation.StringRes;

import com.service.handler.RepoServiceHandler;

public abstract class ServiceHolder {

    private @RepoServiceType int serviceType;
    private Class<? extends RepoServiceHandler> classType;
    private @StringRes int serviceName;

    ServiceHolder(@RepoServiceType int serviceType, Class<? extends RepoServiceHandler> classType, @StringRes int serviceName) {
        this.serviceType = serviceType;
        this.classType = classType;
        this.serviceName = serviceName;
    }

    @RepoServiceType
    public int getServiceType() {
        return serviceType;
    }

    public Class<? extends RepoServiceHandler> getClassType() {
        return classType;
    }

    @StringRes
    public int getServiceName() {
        return serviceName;
    }
}
