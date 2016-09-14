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
import java.util.Map;

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

    public <S> void callListRepositories(@NonNull ListRepositoriesRequest<S> request){

        Observable<List<GitHubRepo>> repositoriesOb = getService().listRepositories(request.getUser(), request.getParams());

        ServiceUtils.scheduleAndObserve(repositoriesOb, (ListRepositoriesRequest<List<GitHubRepo>>)request);
    }

    @Override
    public <S> void exchangeToken(@NonNull ExchangeTokenRequest<S> request) {
        Observable<GitHubAccessToken> accessTokenObservable = getService().exchangeToken(getExchangeTokenUrl(), request.getParams());


        ServiceUtils.scheduleAndObserve(accessTokenObservable,(ExchangeTokenRequest<GitHubAccessToken>)request);
    }

    @NonNull
    @Override
    public String getClientId() {
        if(clientId == null){
            clientId = RepoFetcherApplication.getContext().getString(R.string.github_client_id);
        }

        return clientId;
    }

    @NonNull
    @Override
    public String getClientSecret() {
        if(clientSecret == null){
            clientSecret = RepoFetcherApplication.getContext().getString(R.string.github_client_secret);
        }

        return clientSecret;
    }

    @NonNull
    @Override
    public String getAuthorizationUrl() {
        if(authorizationUrl == null){
            authorizationUrl = RepoFetcherApplication.getContext().getString(R.string.github_authorization_url);
        }

        return authorizationUrl;
    }

    @NonNull
    @Override
    public String getExchangeTokenUrl() {
        if(exchangeTokenUrl == null){
            exchangeTokenUrl = RepoFetcherApplication.getContext().getString(R.string.github_exchange_token_url);
        }

        return exchangeTokenUrl;
    }
}
