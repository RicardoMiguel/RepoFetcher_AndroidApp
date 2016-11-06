package com.service.holder;

import com.repofetcher.R;
import com.service.handler.GitHubServiceHandler;

public class GitHubServiceHolder extends ServiceHolder{

    public GitHubServiceHolder() {
        super(RepoServiceType.GITHUB,
                GitHubServiceHandler.class,
                R.string.github);
    }
}
