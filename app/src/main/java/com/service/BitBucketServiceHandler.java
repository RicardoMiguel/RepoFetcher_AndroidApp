package com.service;

import android.support.annotation.NonNull;

import com.model.github.GitHubRepo;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;

import java.util.List;

import rx.Observable;

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
    public void callListRepositories(@NonNull String user, @NonNull final RepoServiceResponse<?> callback) {
        Observable<List<GitHubRepo>> repositoriesOb = getService().listRepositories(user);
        ServiceUtils.scheduleOnIO_ObserveOnMainThread(repositoriesOb, new SubscriberAdapter<>(callback));
    }
}
