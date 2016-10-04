package com.service.rx;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by ricar on 15/09/2016.
 */
public class RxJavaController<T> {

    @IntDef({MAIN_THREAD, IO, })
    @Retention(RetentionPolicy.SOURCE)
    public @interface SchedulerType {}

    public static final int MAIN_THREAD = 1;
    public static final int IO = 2;

    @Deprecated
    void scheduleOnIO_ObserveOnMainThread(@NonNull Observable<?> observable, @NonNull List<Subscriber> subscribers){
        ConnectableObservable<?> connectableObservable = observable.publish();

        for(Subscriber subscriber: subscribers){
            connectableObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

        connectableObservable.connect();

    }

    public void scheduleAndObserve(@NonNull Observable<T> observable, @NonNull SubscribersMap<T> subscribersMap){
        ConnectableObservable<T> connectableObservable = observable.publish();

        for (Map.Entry<Integer, List<Subscriber<T>>> entry : subscribersMap.getSubscribersList().entrySet())
        {
            for(Subscriber<T> subscriber: entry.getValue()){

                Observable<T> obs = connectableObservable.subscribeOn(Schedulers.io());

                switch (entry.getKey()){
                    case(MAIN_THREAD):
                        obs = obs.observeOn(AndroidSchedulers.mainThread());
                        break;
                    case(IO):
                        obs = obs.observeOn(Schedulers.io());
                        break;
                }

                obs.subscribe(subscriber);
            }
        }

        connectableObservable.connect();
    }

//    public void scheduleAndMerge(List<Observable<T>> list, RepoServiceResponse repoServiceResponse, Action1 onSuccess) {
//        List<Observable<T>> newList = new ArrayList<>();
//        for(Observable<T> observable: list){
//            newList.add(observable.observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io()));
//        }
//        Observable.mergeDelayError(newList)
//                .doAfterTerminate(() -> repoServiceResponse.onSuccess(null))
//                .subscribe(onSuccess, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Log.d("lol","lol_error");
//                    }
//                });
//    }
}
