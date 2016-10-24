package com.service.holder;


import com.repofetcher.R;
import com.service.handler.BitBucketServiceHandler;

/**
 * Created by ricar on 24/10/2016.
 */

public class BitbucketServiceHolder extends ServiceHolder{

    public BitbucketServiceHolder() {
        super(RepoServiceType.BITBUCKET,
                BitBucketServiceHandler.class,
                R.string.bitbucket);
    }
}
