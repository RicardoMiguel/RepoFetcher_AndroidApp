package com.service.handler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.model.ExpirableAccessToken;
import com.model.Owner;
import com.model.github.GitHubAccessToken;
import com.model.github.GitHubOwner;
import com.model.github.GitHubRepo;
import com.repofetcher.R;
import com.service.oauth.OAuthClientManager;
import com.service.request.ExchangeTokenRequest;
import com.service.request.GetOwnRepositoriesRequest;
import com.service.request.GetOwnerRequest;
import com.service.request.GetRepositoriesRequest;
import com.service.request.GitHubExchangeTokenRequest;
import com.service.request.ServiceResponseMapAdapter;
import com.service.rx.RxJavaController;

import java.util.List;

import rx.Observable;

/**
 * Created by ricar on 02/09/2016.
 */
public class GitHubServiceHandler extends RepoServiceHandler<GitHubService>{

    GitHubServiceHandler(@NonNull Context context, @Nullable OAuthClientManager clientManager) {
        super(context, clientManager);
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

    public <S> void callListRepositories(@NonNull GetRepositoriesRequest<S> request){
        Observable<List<GitHubRepo>> repositoriesOb = getService().listRepositories(request.getUser(), request.getParams());
        addSubscribers(request.getHash(), request.getServiceResponseList().getSubscribersList());
        new RxJavaController<List<GitHubRepo>>().scheduleAndObserve(repositoriesOb, (ServiceResponseMapAdapter<List<GitHubRepo>>)request.getServiceResponseList());
    }

    @Override
    public <S> void callListRepositories(@NonNull GetOwnRepositoriesRequest<S> request) {
        Observable<List<GitHubRepo>> repositoriesOb = getService().listRepositories(request.getParams());
        addSubscribers(request.getHash(), request.getServiceResponseList().getSubscribersList());
        new RxJavaController<List<GitHubRepo>>().scheduleAndObserve(repositoriesOb, (ServiceResponseMapAdapter<List<GitHubRepo>>)request.getServiceResponseList());
    }

    @Override
    public <S extends AccessToken> void exchangeToken(@NonNull ExchangeTokenRequest<S> request) {
        if(request instanceof GitHubExchangeTokenRequest) {
            GitHubExchangeTokenRequest gitHubExchangeTokenRequest = (GitHubExchangeTokenRequest)request;

            Observable<GitHubAccessToken> accessTokenObservable = getService().exchangeToken(getExchangeTokenUrl(), request.getParams());
            addSubscribers(request.getHash(), request.getServiceResponseList().getSubscribersList());
            new RxJavaController<GitHubAccessToken>().scheduleAndObserve(accessTokenObservable, gitHubExchangeTokenRequest.getServiceResponseList());
        }
    }

    @Override
    public <S extends ExpirableAccessToken> void refreshToken(@NonNull ExchangeTokenRequest<S> request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Owner> void callGetOwner(@NonNull GetOwnerRequest<S> request) {
        Observable<GitHubOwner> accessTokenObservable = getService().getOwner();
        addSubscribers(request.getHash(), request.getServiceResponseList().getSubscribersList());
        new RxJavaController<GitHubOwner>().scheduleAndObserve(accessTokenObservable, (ServiceResponseMapAdapter<GitHubOwner>)request.getServiceResponseList());
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
