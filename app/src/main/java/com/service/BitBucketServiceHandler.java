package com.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.bitbucket.BitBucketRepositories;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;
import com.service.request.ExchangeTokenRequest;
import com.service.request.ListOwnRepositoriesRequest;
import com.service.request.ListRepositoriesRequest;
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
    public <S> void callListRepositories(@NonNull ListRepositoriesRequest<S> request) {
        Observable<BitBucketRepositories> repositoriesOb = getService().listRepositories(request.getUser());

        new RxJavaController<BitBucketRepositories>().scheduleAndObserve(repositoriesOb, (ServiceResponseMapAdapter<BitBucketRepositories>)request.getServiceResponseList());
    }

    @Override
    public <S> void callListRepositories(@NonNull ListOwnRepositoriesRequest<S> request) {
//        throw new UnsupportedOperationException();
    }

    @Override
    public <S> void exchangeToken(@NonNull ExchangeTokenRequest<S> request) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public String getClientId() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public String getClientSecret() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public String getAuthorizationUrl() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public String getExchangeTokenUrl() {
        throw new UnsupportedOperationException();
    }
}
