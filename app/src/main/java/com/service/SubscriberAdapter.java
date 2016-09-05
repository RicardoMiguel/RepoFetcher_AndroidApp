package com.service;

import android.support.annotation.Nullable;

import rx.Subscriber;

/**
 * Created by ricar on 05/09/2016.
 */
public class SubscriberAdapter<T> extends Subscriber<T> {

    @Nullable
    private RepoServiceResponse<T> response;

    public SubscriberAdapter() {
    }

    public SubscriberAdapter(@Nullable RepoServiceResponse<T> response) {
        this.response = response;
    }

    public SubscriberAdapter(Subscriber<?> subscriber) {
        super(subscriber);
    }

    public SubscriberAdapter(Subscriber<?> subscriber, boolean shareSubscriptions) {
        super(subscriber, shareSubscriptions);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {
        if(this.response != null){
            this.response.onSuccess(t);
        }
    }
}
