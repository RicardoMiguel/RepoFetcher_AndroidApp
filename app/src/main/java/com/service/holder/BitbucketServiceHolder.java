package com.service.holder;


import com.repofetcher.R;
import com.service.handler.BitBucketServiceHandler;


public class BitbucketServiceHolder extends ServiceHolder{

    public BitbucketServiceHolder() {
        super(RepoServiceType.BITBUCKET,
                BitBucketServiceHandler.class,
                R.string.bitbucket);
    }
}
