package com.service;

import android.support.annotation.NonNull;

import com.model.Repo;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;

import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by ricar on 02/09/2016.
 */
public class GitHubServiceHandler extends RepoServiceHandler{

    private GitHubService gitHubService;

    private GitHubService getInstance(){
        if(gitHubService == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RepoFetcherApplication.getContext().getString(R.string.github_base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(client)
                    .build();
            gitHubService = retrofit.create(GitHubService.class);
        }
        return gitHubService;
    }

    public void callListRepositories(@NonNull String user, @NonNull final RepoServiceResponse<List<Repo>> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "all");
        Observable<List<Repo>> repositoriesOb = getInstance().listRepositories(user, params);
        scheduleOnIO_ObserveOnMainThread(repositoriesOb, new Subscriber<List<Repo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Repo> objects) {
                callback.onSuccess(objects);
            }
        });
    }
}
