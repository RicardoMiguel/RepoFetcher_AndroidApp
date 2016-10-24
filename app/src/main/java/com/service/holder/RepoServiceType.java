package com.service.holder;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.service.holder.RepoServiceType.BITBUCKET;
import static com.service.holder.RepoServiceType.GITHUB;

/**
 * Created by ricar on 24/10/2016.
 */
@IntDef({GITHUB, BITBUCKET})
@Retention(RetentionPolicy.SOURCE)
public @interface RepoServiceType {

    int GITHUB = 0;
    int BITBUCKET = 1;

}
