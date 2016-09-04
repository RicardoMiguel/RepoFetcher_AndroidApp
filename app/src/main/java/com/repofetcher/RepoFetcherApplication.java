package com.repofetcher;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by ricar on 02/09/2016.
 */
public class RepoFetcherApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        context = this;
    }

    public static Context getContext(){
        return context;
    }
}
