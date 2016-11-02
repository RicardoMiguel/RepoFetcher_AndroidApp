package com.service.holder;

import android.support.annotation.StringRes;

/**
 * Created by ricar on 24/10/2016.
 */

public abstract class ServiceHolder {

    private @RepoServiceType int serviceType;
    private Class classType;
    private @StringRes int serviceName;

    ServiceHolder(@RepoServiceType int serviceType, Class classType, @StringRes int serviceName) {
        this.serviceType = serviceType;
        this.classType = classType;
        this.serviceName = serviceName;
    }

    @RepoServiceType
    public int getServiceType() {
        return serviceType;
    }

    public Class getClassType() {
        return classType;
    }

    @StringRes
    public int getServiceName() {
        return serviceName;
    }
}
