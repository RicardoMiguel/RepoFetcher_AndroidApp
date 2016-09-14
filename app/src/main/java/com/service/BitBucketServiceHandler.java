package com.service;

import android.support.annotation.NonNull;

import com.model.bitbucket.BitBucketRepositories;
import com.model.github.GitHubRepo;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;
import com.service.request.ListRepositoriesRequest;

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
    public void callListRepositories(@NonNull ListRepositoriesRequest<?> request) {
        Observable<BitBucketRepositories> repositoriesOb = getService().listRepositories(request.getUser());
        Subscriber[] subscribers = {new SubscriberAdapter<>(request.getServiceResponse())};
        addSubscribers(request.getHash(), subscribers);
        ServiceUtils.scheduleOnIO_ObserveOnMainThread(repositoriesOb, subscribers);
    }
}
