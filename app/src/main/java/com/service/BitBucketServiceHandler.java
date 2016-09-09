package com.service;

import android.support.annotation.NonNull;

import com.model.bitbucket.BitBucketRepositories;
import com.model.github.GitHubRepo;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;

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
    public void callListRepositories(int hash, @NonNull String user, @NonNull final RepoServiceResponse<?> callback) {
        Observable<BitBucketRepositories> repositoriesOb = getService().listRepositories(user);
        Subscriber[] subscribers = {new SubscriberAdapter<>(callback)};
        addSubscribers(hash, subscribers);
        ServiceUtils.scheduleOnIO_ObserveOnMainThread(repositoriesOb, subscribers);
    }
}
