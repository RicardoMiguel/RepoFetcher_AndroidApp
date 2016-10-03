package com.service.handler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.service.SubscriberService;
import com.service.interceptor.JsonInterceptor;
import com.service.interceptor.OAuthInterceptor;
import com.service.oauth.OAuthClientManager;
import com.service.oauth.OAuthClientService;

import java.util.ArrayList;
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
public abstract class RepoServiceHandler<T> implements IRepoServiceHandler, SubscriberService, OAuthClientService {

    private T service;

    @NonNull protected Context context;

    @Nullable protected String clientId;
    @Nullable protected String clientSecret;
    @Nullable protected String authorizationUrl;
    @Nullable protected String exchangeTokenUrl;
    @Nullable protected OAuthClientManager sessionManager;

    @Nullable private Map<Integer, List<Subscriber>> listToUnsubscribe;

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

    <S> void addSubscribers(int id, Map<Integer, List<Subscriber<S>>> subscribersMap) {
        for (Map.Entry<Integer, List<Subscriber<S>>> entry : subscribersMap.entrySet()) {
            addSubscribers(id, new ArrayList<>(entry.getValue()));
        }
    }

    public void addSubscribers(int id, List<Subscriber> subscribers) {
        Map<Integer, List<Subscriber>> listMap = getListToUnsubscribe();
        List<Subscriber> subscriber = listMap.get(id);
        if(subscriber == null){
            listMap.put(id, subscribers);
        } else {
            subscriber.addAll(subscribers);
        }
    }

    @Override
    public void removeSubscribers(int id) {
        List<Subscriber> subscribers = getListToUnsubscribe().remove(id);
        if(subscribers != null) {
            for (Subscriber subscriber : subscribers) {
                subscriber.unsubscribe();
            }
        }
    }

    @NonNull
    private Map<Integer, List<Subscriber>> getListToUnsubscribe(){
        if(listToUnsubscribe == null){
            listToUnsubscribe = new HashMap<>();
        }
        return listToUnsubscribe;
    }

    public OAuthClientManager getOAuthClientManager(){
        return sessionManager;
    }
}