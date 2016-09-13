package com.service;

import android.support.annotation.NonNull;

import com.model.github.GitHubAccessToken;
import com.model.github.GitHubRepo;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;
import com.service.request.ExchangeTokenRequest;
import com.service.request.ListRepositoriesRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

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

    public void callListRepositories(@NonNull ListRepositoriesRequest<?> request){

        Observable<List<GitHubRepo>> repositoriesOb = getService().listRepositories(request.getUser(), request.getParams());

        Subscriber[] subscribers = {new SubscriberAdapter<>(request.getServiceResponse())};
        addSubscribers(request.getHash(), subscribers);
        ServiceUtils.scheduleOnIO_ObserveOnMainThread(repositoriesOb, subscribers);
    }

    @Override
    public void exchangeToken(@NonNull ExchangeTokenRequest<?> request) {
        Observable<GitHubAccessToken> accessTokenObservable = getService().exchangeToken("https://github.com/login/oauth/access_token", request.getParams());

        Subscriber[] subscribers = {new SubscriberAdapter<>(request.getServiceResponse())};
        addSubscribers(request.getHash(), subscribers);
        ServiceUtils.scheduleOnIO_ObserveOnMainThread(accessTokenObservable, subscribers);
    }
}
