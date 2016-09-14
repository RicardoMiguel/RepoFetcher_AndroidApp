package com.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by ricar on 04/09/2016.
 */
abstract class RepoServiceHandler<T> implements IRepoServiceHandler, SubscriberService {

    private T service;

    @Nullable private Map<Integer, List<Subscriber>> listToUnsubscribe;

    protected T getService(){
        if(service == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getServiceBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(client)
                    .build();
            service = retrofit.create(getServiceClassSpecification());
        }
        return service;
    }

    protected abstract Class<T> getServiceClassSpecification();

    @NonNull
    protected abstract String getServiceBaseUrl();


    @Override
    public void addSubscribers(int id, Subscriber... subscribers) {
        Map<Integer, List<Subscriber>> listMap = getListToUnsubscribe();
        List<Subscriber> subscriber = listMap.get(id);
        if(subscriber == null){
            listMap.put(id, new ArrayList<>(Arrays.asList(subscribers)));
        } else {
            subscriber.addAll(Arrays.asList(subscribers));
        }
    }

    @Override
    public void removeSubscribers(int id) {
        List<Subscriber> subscribers = getListToUnsubscribe().remove(id);
        for(Subscriber subscriber : subscribers){
            subscriber.unsubscribe();
        }
    }

    @NonNull
    private Map<Integer, List<Subscriber>> getListToUnsubscribe(){
        if(listToUnsubscribe == null){
            listToUnsubscribe = new HashMap<>();
        }
        return listToUnsubscribe;
    }
}
