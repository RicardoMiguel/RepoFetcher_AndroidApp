package com.service.holder;

import com.repofetcher.R;
import com.service.FetcherCallsHandler;
import com.service.handler.GitHubServiceHandler;

/**
 * Created by ricar on 24/10/2016.
 */

public class GitHubServiceHolder extends ServiceHolder{


    public GitHubServiceHolder() {
        super(FetcherCallsHandler.GITHUB, GitHubServiceHandler.class, R.string.github);
    }
}
