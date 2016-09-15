package com.service.rx;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by ricar on 15/09/2016.
 */
public class SubscribersMap<T> {

    @Nullable
    private Map<Integer, List<Subscriber<T>>> subscribers;

    public SubscribersMap(){

    }

    public void add(@RxJavaController.SchedulerType int type, @NonNull Subscriber<T> subscriber){
        Map<Integer, List<Subscriber<T>>> serviceResponseList = getSubscribersList();

        List<Subscriber<T>> list = serviceResponseList.get(type);

        if(list == null){
            list = new ArrayList<>();
        }

        list.add(subscriber);
        serviceResponseList.put(type, list);
    }

    @NonNull
    public Map<Integer, List<Subscriber<T>>> getSubscribersList(){
        if(this.subscribers == null){
            this.subscribers = new HashMap<>();
        }
        return subscribers;
    }
}
