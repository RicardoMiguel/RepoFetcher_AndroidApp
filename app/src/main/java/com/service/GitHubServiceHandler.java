package com.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.github.GitHubAccessToken;
import com.model.github.GitHubRepo;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;
import com.service.request.ExchangeTokenRequest;
import com.service.request.ListOwnRepositoriesRequest;
import com.service.request.ListRepositoriesRequest;
import com.service.request.ServiceResponseMapAdapter;
import com.service.rx.RxJavaController;

import java.util.List;

import rx.Observable;

/**
 * Created by ricar on 02/09/2016.
 */
class GitHubServiceHandler extends RepoServiceHandler<GitHubService>{

    public GitHubServiceHandler(@NonNull Context context, @Nullable OAuthClientRequester oAuthClientRequester) {
        super(context, oAuthClientRequester);
    }

    @Override
    protected Class<GitHubService> getServiceClassSpecification() {
        return GitHubService.class;
    }

    @NonNull
    @Override
    protected String getServiceBaseUrl() {
        return context.getString(R.string.github_base_url);
    }

    public <S> void callListRepositories(@NonNull ListRepositoriesRequest<S> request){
        Observable<List<GitHubRepo>> repositoriesOb = getService().listRepositories(request.getUser(), request.getParams());

        new RxJavaController<List<GitHubRepo>>().scheduleAndObserve(repositoriesOb, (ServiceResponseMapAdapter<List<GitHubRepo>>)request.getServiceResponseList());
    }

    @Override
    public <S> void callListRepositories(@NonNull ListOwnRepositoriesRequest<S> request) {
        Observable<List<GitHubRepo>> repositoriesOb = getService().listRepositories(request.getParams());

        new RxJavaController<List<GitHubRepo>>().scheduleAndObserve(repositoriesOb, (ServiceResponseMapAdapter<List<GitHubRepo>>)request.getServiceResponseList());
    }

    @Override
    public <S> void exchangeToken(@NonNull ExchangeTokenRequest<S> request) {
        Observable<GitHubAccessToken> accessTokenObservable = getService().exchangeToken(getExchangeTokenUrl(), request.getParams());

        new RxJavaController<GitHubAccessToken>().scheduleAndObserve(accessTokenObservable,(ServiceResponseMapAdapter<GitHubAccessToken>)request.getServiceResponseList());
    }

    @NonNull
    @Override
    public String getClientId() {
        if(clientId == null){
            clientId = context.getString(R.string.github_client_id);
        }

        return clientId;
    }

    @NonNull
    @Override
    public String getClientSecret() {
        if(clientSecret == null){
            clientSecret = context.getString(R.string.github_client_secret);
        }

        return clientSecret;
    }

    @NonNull
    @Override
    public String getAuthorizationUrl() {
        if(authorizationUrl == null){
            authorizationUrl = context.getString(R.string.github_authorization_url);
        }

        return authorizationUrl;
    }

    @NonNull
    @Override
    public String getExchangeTokenUrl() {
        if(exchangeTokenUrl == null){
            exchangeTokenUrl = context.getString(R.string.github_exchange_token_url);
        }

        return exchangeTokenUrl;
    }
}
