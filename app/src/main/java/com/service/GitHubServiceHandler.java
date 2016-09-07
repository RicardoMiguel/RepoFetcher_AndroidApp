package com.service;

import android.support.annotation.NonNull;

import com.model.github.GitHubRepo;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by ricar on 02/09/2016.
 */
class GitHubServiceHandler extends RepoServiceHandler<GitHubService>{

    GitHubServiceHandler() {
    }

    @Override
    protected Class<GitHubService> getServiceClassSpecification() {
        return GitHubService.class;
    }

    @NonNull
    @Override
    protected String getServiceBaseUrl() {
        return RepoFetcherApplication.getContext().getString(R.string.github_base_url);
    }

    public void callListRepositories(@NonNull String user, @NonNull final RepoServiceResponse<?> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "all");
        Observable<List<GitHubRepo>> repositoriesOb = getService().listRepositories(user, params);
        ServiceUtils.scheduleOnIO_ObserveOnMainThread(repositoriesOb, new SubscriberAdapter<>(callback));
    }
}
