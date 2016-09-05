package com.service;

import rx.Subscriber;

/**
 * Created by ricar on 05/09/2016.
 */
public abstract class SubscriberAdapter<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }
}
