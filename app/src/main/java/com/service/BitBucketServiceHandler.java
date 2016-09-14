package com.service;

import android.support.annotation.NonNull;

import com.model.bitbucket.BitBucketRepositories;
import com.model.github.GitHubRepo;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;
import com.service.request.ExchangeTokenRequest;
import com.service.request.ListRepositoriesRequest;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ricar on 06/09/2016.
 */
public class BitBucketServiceHandler extends RepoServiceHandler<BitBucketService>{

    BitBucketServiceHandler() {
    }

    @Override
    protected Class<BitBucketService> getServiceClassSpecification() {
        return BitBucketService.class;
    }

    @NonNull
    @Override
    protected String getServiceBaseUrl() {
        return RepoFetcherApplication.getContext().getString(R.string.bitbucket_base_url);
    }

    @Override
    public <S> void callListRepositories(@NonNull ListRepositoriesRequest<S> request) {
        Observable<BitBucketRepositories> repositoriesOb = getService().listRepositories(request.getUser());

        ServiceUtils.scheduleAndObserve(repositoriesOb, (ListRepositoriesRequest<BitBucketRepositories>)request);
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
