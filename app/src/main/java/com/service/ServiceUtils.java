package com.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by ricar on 04/09/2016.
 */
public class ServiceUtils {

    static void scheduleOnIO_ObserveOnMainThread(@NonNull Observable<?> observable, @NonNull Subscriber<?>... subscribers){
        ConnectableObservable<?> connectableObservable = observable.publish();

        for(Subscriber subscriber: subscribers){
            connectableObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

        connectableObservable.connect();

    }

    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void runIfInstanceNotNull(@Nullable Object instance, @NonNull Runnable runnable){
        if(instance != null){
            runnable.run();
        }
    }
}
