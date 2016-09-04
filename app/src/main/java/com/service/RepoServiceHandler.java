package com.service;

import android.support.annotation.NonNull;

import com.model.Repo;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;

/**
 * Created by ricar on 04/09/2016.
 */
public abstract class RepoServiceHandler implements RepoService{

    @Override
    public abstract void callListRepositories(@NonNull String user, @NonNull RepoServiceResponse<List<Repo>> callback);

    protected final void scheduleOnIO_ObserveOnMainThread(@NonNull Observable<?> observable, @NonNull Subscriber<?>... subscribers){
        ConnectableObservable<?> connectableObservable = observable.publish();

        for(Subscriber subscriber: subscribers){
            connectableObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

        connectableObservable.connect();

    }
}
