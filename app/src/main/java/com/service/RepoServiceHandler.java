package com.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.model.Owner;
import com.service.interceptor.JsonInterceptor;
import com.service.interceptor.OAuthInterceptor;
import com.service.oauth.OAuthClientManager;
import com.service.oauth.OAuthClientRequester;
import com.service.oauth.OAuthClientService;
import com.service.oauth.OAuthSessionManager;

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
abstract class RepoServiceHandler<T> implements IRepoServiceHandler, SubscriberService, OAuthClientService, OAuthClientManager {

    private T service;

    @NonNull protected Context context;

    @Nullable protected String clientId;
    @Nullable protected String clientSecret;
    @Nullable protected String authorizationUrl;
    @Nullable protected String exchangeTokenUrl;
    protected OAuthSessionManager sessionManager;


    @Nullable private Map<Integer, List<Subscriber>> listToUnsubscribe;

    protected RepoServiceHandler(@NonNull Context context, @Nullable OAuthClientRequester oAuthClientRequester){
        this.context = context;
        this.sessionManager = new OAuthSessionManager(this, oAuthClientRequester);
    }

    protected T getService(){
        if(service == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(new JsonInterceptor())
                    .addInterceptor(new OAuthInterceptor(sessionManager))
                    .build();

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

    public void setOwner(@Nullable Owner owner){
        sessionManager.setOwner(owner);
    }

    @Nullable public Owner getOwner(){
        return sessionManager.getOwner();
    }

    public void setAccessToken(@Nullable AccessToken token){
        sessionManager.setAccessToken(token);
    }

    @Nullable public AccessToken getToken(){
        return sessionManager.getToken();
    }
}
