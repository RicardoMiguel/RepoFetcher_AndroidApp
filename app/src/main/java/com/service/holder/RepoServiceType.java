package com.service.holder;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.service.holder.RepoServiceType.BITBUCKET;
import static com.service.holder.RepoServiceType.GITHUB;

@IntDef({GITHUB, BITBUCKET})
@Retention(RetentionPolicy.SOURCE)
public @interface RepoServiceType {

    final static int GITHUB = 0;
    final static int BITBUCKET = 1;

}
