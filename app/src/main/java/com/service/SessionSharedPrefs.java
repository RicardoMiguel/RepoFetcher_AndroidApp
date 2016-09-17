package com.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.model.Owner;
import com.model.bitbucket.BitBucketOwner;
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

    void saveToken(String file, String token){
        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.TOKEN, token);
        editor.commit();
    }

    @Nullable
    Map<Class, String> getTokens(){
        Map<Class, String> map = null;
        Class[] classes = getClasses();

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

    boolean hasTokens(){
        boolean tokenFound = false;
        Class[] classes = getClasses();
        for(Class c : classes){
            if(!TextUtils.isEmpty(getToken(c.getName()))){
                tokenFound = true;
            }
        }
        return tokenFound;
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
