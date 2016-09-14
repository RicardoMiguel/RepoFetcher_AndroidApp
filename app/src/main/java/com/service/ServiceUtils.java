package com.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.service.request.BaseRequest;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by ricar on 04/09/2016.
 */
public class ServiceUtils {

    static void scheduleOnIO_ObserveOnMainThread(@NonNull Observable<?> observable, @NonNull List<Subscriber> subscribers){
        ConnectableObservable<?> connectableObservable = observable.publish();

        for(Subscriber subscriber: subscribers){
            connectableObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

        connectableObservable.connect();

    }

    static <S> void scheduleAndObserve(@NonNull Observable<S> observable, BaseRequest<S> request){
        Map<Integer, List<RepoServiceResponse<S>>> subscribers = request.getServiceResponseList();

        ConnectableObservable<S> connectableObservable = observable.publish();

        for (Map.Entry<Integer, List<RepoServiceResponse<S>>> entry : subscribers.entrySet())
        {
            for(RepoServiceResponse<S> serviceResponse: entry.getValue()){

                Observable<S> obs = connectableObservable.subscribeOn(Schedulers.io());

                switch (entry.getKey()){
                    case(BaseRequest.MAIN_THREAD):
                        obs = obs.observeOn(AndroidSchedulers.mainThread());
                        break;
                    case(BaseRequest.IO):
                        obs = obs.observeOn(Schedulers.io());
                        break;
                }

                obs.subscribe(new SubscriberAdapter<S>(serviceResponse));
            }
        }

        connectableObservable.connect();
    }

    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
