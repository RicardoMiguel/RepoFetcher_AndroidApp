package com.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.model.ExpirableAccessToken;
import com.model.Owner;
import com.model.bitbucket.BitBucketAccessToken;
import com.model.bitbucket.BitBucketOwner;
import com.model.bitbucket.BitBucketRepositories;
import com.repofetcher.R;
import com.service.oauth.OAuthClientRequester;
import com.service.request.BitbucketExchangeTokenRequest;
import com.service.request.BitbucketRefreshTokenRequest;
import com.service.request.ExchangeTokenRequest;
import com.service.request.GetOwnRepositoriesRequest;
import com.service.request.GetOwnerRequest;
import com.service.request.GetRepositoriesRequest;
import com.service.request.ServiceResponseMapAdapter;
import com.service.rx.RxJavaController;

import rx.Observable;

/**
 * Created by ricar on 06/09/2016.
 */
public class BitBucketServiceHandler extends RepoServiceHandler<BitBucketService>{

    public BitBucketServiceHandler(@NonNull Context context, @Nullable OAuthClientRequester oAuthClientRequester) {
        super(context, oAuthClientRequester);
    }

    @Override
    protected Class<BitBucketService> getServiceClassSpecification() {
        return BitBucketService.class;
    }

    @NonNull
    @Override
    protected String getServiceBaseUrl() {
        return context.getString(R.string.bitbucket_base_url);
    }

    @Override
    public <S> void callListRepositories(@NonNull GetRepositoriesRequest<S> request) {
        Observable<BitBucketRepositories> repositoriesOb = getService().listRepositories(request.getUser());
        addSubscribers(request.getHash(), request.getServiceResponseList().getSubscribersList());
        new RxJavaController<BitBucketRepositories>().scheduleAndObserve(repositoriesOb, (ServiceResponseMapAdapter<BitBucketRepositories>)request.getServiceResponseList());
    }

    @Override
    public <S> void callListRepositories(@NonNull GetOwnRepositoriesRequest<S> request) {
        if(sessionManager.getOwner() != null) {
            Observable<BitBucketRepositories> repositoriesOb = getService().listRepositories(sessionManager.getOwner().getLogin());
            addSubscribers(request.getHash(), request.getServiceResponseList().getSubscribersList());
            new RxJavaController<BitBucketRepositories>().scheduleAndObserve(repositoriesOb, (ServiceResponseMapAdapter<BitBucketRepositories>) request.getServiceResponseList());
        }
    }

    @Override
    public <S extends AccessToken> void exchangeToken(@NonNull ExchangeTokenRequest<S> request) {
        if(request instanceof BitbucketExchangeTokenRequest) {
            BitbucketExchangeTokenRequest castedRequest = (BitbucketExchangeTokenRequest) request;

            Observable<BitBucketAccessToken> accessTokenObservable = getService().exchangeToken(getExchangeTokenUrl(),
                    castedRequest.getBasicAuthorization(),
                    castedRequest.getAuthorizationGrant(),
                    castedRequest.getCode());
            addSubscribers(request.getHash(), request.getServiceResponseList().getSubscribersList());
            new RxJavaController<BitBucketAccessToken>().scheduleAndObserve(accessTokenObservable, castedRequest.getServiceResponseList());
        }
    }

    public <S extends ExpirableAccessToken> void refreshToken(@NonNull ExchangeTokenRequest<S> request){
        if(request instanceof BitbucketRefreshTokenRequest) {
            BitbucketRefreshTokenRequest castedRequest = (BitbucketRefreshTokenRequest) request;

            Observable<BitBucketAccessToken> accessTokenObservable = getService().refreshToken(getExchangeTokenUrl(),
                    castedRequest.getBasicAuthorization(),
                    castedRequest.getAuthorizationGrant(),
                    castedRequest.getCode());
            addSubscribers(request.getHash(), request.getServiceResponseList().getSubscribersList());
            new RxJavaController<BitBucketAccessToken>().scheduleAndObserve(accessTokenObservable, castedRequest.getServiceResponseList());
        }
    }

    @Override
    public <S extends Owner> void callGetOwner(@NonNull GetOwnerRequest<S> request) {
        Observable<BitBucketOwner> repositoriesOb = getService().getOwner();
        addSubscribers(request.getHash(), request.getServiceResponseList().getSubscribersList());
        new RxJavaController<BitBucketOwner>().scheduleAndObserve(repositoriesOb, (ServiceResponseMapAdapter<BitBucketOwner>)request.getServiceResponseList());
    }


    @NonNull
    @Override
    public String getClientId() {
        if(clientId == null){
            clientId = context.getString(R.string.bitbucket_client_id);
        }

        return clientId;
    }

    @NonNull
    @Override
    public String getClientSecret() {
        if(clientSecret == null){
            clientSecret = context.getString(R.string.bitbucket_client_secret);
        }

        return clientSecret;
    }

    @NonNull
    @Override
    public String getAuthorizationUrl() {
        if(authorizationUrl == null){
            authorizationUrl = context.getString(R.string.bitbucket_authorization_url);
        }

        return authorizationUrl;
    }

    @NonNull
    @Override
    public String getExchangeTokenUrl() {
        if(exchangeTokenUrl == null){
            exchangeTokenUrl = context.getString(R.string.bitbucket_exchange_token_url);
        }

        return exchangeTokenUrl;
    }
}
