package com.service.request;

import android.support.annotation.NonNull;

import com.service.RepoServiceResponse;
import com.service.SubscriberAdapter;
import com.service.rx.RxJavaController;
import com.service.rx.SubscribersMap;

public class ServiceResponseMapAdapter<T> extends SubscribersMap<T>{

    public void add(@RxJavaController.SchedulerType int type, @NonNull RepoServiceResponse<T> serviceResponse) {
        super.add(type, new SubscriberAdapter<>(serviceResponse));
    }
}
