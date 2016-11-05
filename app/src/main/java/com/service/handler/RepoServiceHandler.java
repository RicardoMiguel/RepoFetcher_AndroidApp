package com.service.handler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.service.SubscriberService;
import com.service.interceptor.JsonInterceptor;
import com.service.interceptor.OAuthInterceptor;
import com.service.oauth.OAuthClientManager;
import com.service.oauth.OAuthClientService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public abstract class RepoServiceHandler<T> implements IRepoServiceHandler, SubscriberService, OAuthClientService {

    private T service;

    @NonNull protected Context context;

    @Nullable protected String clientId;
    @Nullable protected String clientSecret;
    @Nullable protected String authorizationUrl;
    @Nullable protected String exchangeTokenUrl;
    @Nullable protected OAuthClientManager sessionManager;

    @Nullable private SparseArray<List<Subscriber>> listToUnsubscribe;

    protected RepoServiceHandler(@NonNull Context context, @Nullable OAuthClientManager clientManager){
        this.context = context;
        this.sessionManager = clientManager;
    }

    protected T getService(){
        if(service == null){
            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(new JsonInterceptor());
                    if(sessionManager != null) {
                        httpBuilder.addInterceptor(new OAuthInterceptor(sessionManager));
                    }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getServiceBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(httpBuilder.build())
                    .build();
            service = retrofit.create(getServiceClassSpecification());
        }
        return service;
    }

    protected abstract Class<T> getServiceClassSpecification();

    @NonNull
    protected abstract String getServiceBaseUrl();

    <S> void addSubscribers(int id, SparseArray<List<Subscriber<S>>> subscribersMap) {
        for (int i = 0; i < subscribersMap.size(); i++) {
            int key = subscribersMap.keyAt(i);
            addSubscribers(id, new ArrayList<>(subscribersMap.get(key)));
        }
    }

    public void addSubscribers(int id, List<Subscriber> subscribers) {
        SparseArray<List<Subscriber>> listMap = getListToUnsubscribe();
        List<Subscriber> subscriber = listMap.get(id);
        if(subscriber == null){
            listMap.put(id, subscribers);
        } else {
            subscriber.addAll(subscribers);
        }
    }

    @Override
    public void removeSubscribers(int id) {
        SparseArray<List<Subscriber>> listSparseArray = getListToUnsubscribe();
        List<Subscriber> subscribers = listSparseArray.get(id);
        if(subscribers != null) {
            for (Subscriber subscriber : subscribers) {
                subscriber.unsubscribe();
            }
        }
        listSparseArray.remove(id);
    }

    @NonNull
    private SparseArray<List<Subscriber>> getListToUnsubscribe(){
        if(listToUnsubscribe == null){
            listToUnsubscribe = new SparseArray<>();
        }
        return listToUnsubscribe;
    }

    public OAuthClientManager getOAuthClientManager(){
        return sessionManager;
    }
}
