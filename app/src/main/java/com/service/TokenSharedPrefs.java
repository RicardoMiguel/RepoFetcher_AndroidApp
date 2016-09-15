package com.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.repofetcher.RepoFetcherApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricar on 15/09/2016.
 */
public class TokenSharedPrefs {

    public static final Class GITHUB = GitHubServiceHandler.class;

    public static final Class BITBUCKET = BitBucketServiceHandler.class;

    private Context context;

    TokenSharedPrefs(@NonNull Context context){
        this.context = context;
    }

    void saveToken(String file, String token){
        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.TOKEN, token);
        editor.commit();
    }

    @Nullable
    Map<Class, String> getTokens(){
        Map<Class, String> map = null;
        Class[] classes = {GITHUB, BITBUCKET};

        for(Class c : classes){
            String token = getToken(c.getName());
            if(!TextUtils.isEmpty(token)){
                if(map == null){
                    map = new HashMap<>();
                }
                map.put(c, token);
            }
        }
        return map;
    }

    @Nullable
    String getToken(String file){
        return context.getSharedPreferences(file, Context.MODE_PRIVATE).getString(Constants.TOKEN, null);
    }
}
