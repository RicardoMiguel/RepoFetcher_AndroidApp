package com.service;

import android.support.annotation.NonNull;

import com.model.Repo;
import com.repofetcher.R;
import com.repofetcher.RepoFetcherApplication;

import java.util.List;

/**
 * Created by ricar on 06/09/2016.
 */
public class BitBucketServiceHandler extends RepoServiceHandler<BitBucketService>{

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
    public void callListRepositories(@NonNull String user, @NonNull RepoServiceResponse<List<Repo>> callback) {

    }
}
