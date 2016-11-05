package com.service.rx;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class SubscribersMap<T> {

    @Nullable
    private SparseArray<List<Subscriber<T>>> subscribers;

    public SubscribersMap(){

    }

    public void add(@RxJavaController.SchedulerType int type, @NonNull Subscriber<T> subscriber){
        SparseArray<List<Subscriber<T>>> serviceResponseList = getSubscribersList();

        List<Subscriber<T>> list = serviceResponseList.get(type, new ArrayList<>());

        list.add(subscriber);
        serviceResponseList.put(type, list);
    }

    @NonNull
    public SparseArray<List<Subscriber<T>>> getSubscribersList(){
        if(this.subscribers == null){
            this.subscribers = new SparseArray<>();
        }
        return subscribers;
    }
}
