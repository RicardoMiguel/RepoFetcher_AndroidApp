package com.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.model.AccessToken;
import com.model.Owner;
import com.model.bitbucket.BitBucketAccessToken;
import com.model.bitbucket.BitBucketOwner;
import com.model.github.GitHubAccessToken;
import com.model.github.GitHubOwner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricar on 15/09/2016.
 */
public class SessionSharedPrefs {

    public static final Class GITHUB = GitHubServiceHandler.class;

    public static final Class BITBUCKET = BitBucketServiceHandler.class;

    private static Class[] getClasses(){
        Class[] classes = {GITHUB, BITBUCKET};
        return classes;
    }

    private Context context;

    SessionSharedPrefs(@NonNull Context context){
        this.context = context;
    }

    void saveToken(@NonNull String file, @Nullable String token){
        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.TOKEN, token);
        editor.commit();
    }

    @Nullable
    Map<Class, AccessToken> getTokens(){
        Map<Class, AccessToken> map = null;
        Class[] classes = getClasses();

        for(Class c : classes){
            AccessToken token = getToken(c.getName());
            if(token != null){
                if(map == null){
                    map = new HashMap<>();
                }
                map.put(c, token);
            }
        }
        return map;
    }

    @Nullable
    AccessToken getToken(@NonNull String file){
        String token = context.getSharedPreferences(file, Context.MODE_PRIVATE).getString(Constants.TOKEN, null);
        AccessToken accessToken = null;
        if(token != null) {
            if (file.equals(GITHUB.getName())) {
                accessToken = new GitHubAccessToken();
            } else if (file.equals(BITBUCKET.getName())) {
                accessToken = new BitBucketAccessToken();
            }
            accessToken.setAccessToken(token);
        }
        return accessToken;
    }

    void saveOwner(String file, @NonNull Owner owner){
        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.USERNAME, owner.getLogin());
        editor.commit();
    }

    @Nullable
    Owner getOwner(@NonNull String file){
        Owner owner = null;
        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        String username = sharedPref.getString(Constants.USERNAME,null);
        if(username != null){
            if(file.equals(GITHUB.getName())){
                owner = new GitHubOwner();
            } else if(file.equals(BITBUCKET.getName())){
                owner = new BitBucketOwner();
            }
            owner.setLogin(username);
        }
        return owner;
    }

    @Nullable
    Map<Class, Owner> getOwners(){
        Map<Class, Owner> owners = null;
        Class[] classes = getClasses();
        for(Class c : classes){
            Owner owner = getOwner(c.getName());
            if(owner != null){
                if(owners == null){
                    owners = new HashMap<>();
                }
                owners.put(c,owner);
            }
        }
        return owners;
    }
}
