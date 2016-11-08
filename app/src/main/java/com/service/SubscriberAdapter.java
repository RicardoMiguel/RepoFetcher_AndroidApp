package com.service;

import android.support.annotation.Nullable;

import rx.Subscriber;

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
        //Intentional empty method. Not needed so far.
    }

    @Override
    public void onError(Throwable e) {
        if(this.response != null){
            this.response.onError(e);
        }
    }

    @Override
    public void onNext(T t) {
        if(this.response != null){
            this.response.onSuccess(t);
        }
    }

    @Nullable
    public RepoServiceResponse<T> getResponse() {
        return response;
    }
}
