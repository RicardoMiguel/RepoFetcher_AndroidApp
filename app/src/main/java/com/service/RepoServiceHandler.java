package com.service;

import android.support.annotation.NonNull;

import com.model.Repo;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by ricar on 04/09/2016.
 */
abstract class RepoServiceHandler<T> implements IRepoServiceHandler {

    private T service;

    protected T getService(){
        if(service == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RepoFetcherApplication.getContext().getString(R.string.github_base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(client)
                    .build();
            service = retrofit.create(getServiceClassSpecification());
        }
        return service;
    }

    protected abstract Class<T> getServiceClassSpecification();

    @Override
    public abstract void callListRepositories(@NonNull String user, @NonNull RepoServiceResponse<List<Repo>> callback);


}
