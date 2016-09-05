package com.service;

import android.support.annotation.NonNull;

import com.model.Repo;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by ricar on 02/09/2016.
 */
class GitHubServiceHandler extends RepoServiceHandler<GitHubService>{

    @Override
    protected Class<GitHubService> getServiceClassSpecification() {
        return GitHubService.class;
    }

    public void callListRepositories(@NonNull String user, @NonNull final RepoServiceResponse<List<Repo>> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "all");
        Observable<List<Repo>> repositoriesOb = getService().listRepositories(user, params);
        ServiceUtils.scheduleOnIO_ObserveOnMainThread(repositoriesOb, new SubscriberAdapter<List<Repo>>() {
            @Override
            public void onNext(List<Repo> repos) {
                callback.onSuccess(repos);
            }
        });
    }
}
