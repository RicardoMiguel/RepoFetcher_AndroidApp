package com.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.service.interceptor.JsonInterceptor;
import com.service.interceptor.OAuthInterceptor;
import com.service.request.BaseRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by ricar on 04/09/2016.
 */
abstract class RepoServiceHandler<T> implements IRepoServiceHandler, SubscriberService, OAuthClientService {

    private T service;

    @Nullable protected String clientId;
    @Nullable protected String clientSecret;
    @Nullable protected String authorizationUrl;
    @Nullable protected String exchangeTokenUrl;
    @Nullable private String token;

    @Nullable private Map<Integer, List<Subscriber>> listToUnsubscribe;

    protected T getService(){
        if(service == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(new JsonInterceptor())
                    .addInterceptor(new OAuthInterceptor(this))
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


    @Override
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

    @Nullable
    public String getOAuthToken(){
        return token;
    }

    public void setOAuthToken(String token){
        this.token = token;
    }

    protected <S> void makeCall(Observable<S> observable, BaseRequest<S> request){
        request.addServiceResponse(request.getUiServiceResponse());
        List<Subscriber> subscriberAdapterList = new ArrayList<>();
        for(RepoServiceResponse<S> serviceResponse : request.getServiceResponseList()){
            subscriberAdapterList.add(new SubscriberAdapter<>(serviceResponse));
        }

        addSubscribers(request.getHash(), subscriberAdapterList);
        ServiceUtils.scheduleOnIO_ObserveOnMainThread(observable, subscriberAdapterList);
    }

}
