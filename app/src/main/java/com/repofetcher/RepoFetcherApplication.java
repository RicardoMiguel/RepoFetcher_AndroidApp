package com.repofetcher;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

public class RepoFetcherApplication extends Application{
    private static RepoFetcherApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        application = this;
    }

    public static Context getContext(){
        return application;
    }
}
