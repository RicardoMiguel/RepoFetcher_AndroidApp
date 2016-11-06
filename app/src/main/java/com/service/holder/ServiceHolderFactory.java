package com.service.holder;

public class ServiceHolderFactory {

    public ServiceHolder create(@RepoServiceType int service){
        switch(service){
            case RepoServiceType.GITHUB:
                return new GitHubServiceHolder();
            case RepoServiceType.BITBUCKET:
                return new BitbucketServiceHolder();
        }
        return null;
    }

}
