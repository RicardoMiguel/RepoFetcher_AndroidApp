package com.service.oauth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.model.AccessToken;
import com.model.ExpirableAccessToken;
import com.model.Owner;
import com.model.bitbucket.BitBucketAccessToken;
import com.model.bitbucket.BitBucketOwner;
import com.model.github.GitHubAccessToken;
import com.model.github.GitHubOwner;
import com.service.handler.BitBucketServiceHandler;
import com.service.Constants;
import com.service.handler.GitHubServiceHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * This class uses currentThread to write into SharedPreferences.
 *
 * Created by ricar on 15/09/2016.
 */
public class SessionSharedPrefs {

    public static final Class GITHUB = GitHubServiceHandler.class;

    public static final Class BITBUCKET = BitBucketServiceHandler.class;

    private static final String TOKEN = Constants.TOKEN;

    private static final String EXPIRATION = "expiration";

    private static final String REFRESH_TOKEN = Constants.REFRESH_TOKEN;

    private static final String DATE_ACQUIRED = "date_acquired";

    private static final String USERNAME = Constants.USERNAME;

    private static Class[] getClasses(){
        return new Class[]{GITHUB, BITBUCKET};
    }

    private Context context;

    public SessionSharedPrefs(@NonNull Context context){
        this.context = context;
    }

    /**
     * This method uses currentThread to write into SharedPreferences.
     *
     * @param file The shared prefs to write into.
     * @param token The AccessToken to write into.
     */
    @SuppressLint("CommitPrefEdits")
    public void saveToken(@NonNull String file, @Nullable AccessToken token){
        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(token != null){
            if(token instanceof ExpirableAccessToken){
                ExpirableAccessToken expirableAccessToken = (ExpirableAccessToken) token;
                editor.putInt(EXPIRATION, expirableAccessToken.getExpiresIn());
                editor.putString(REFRESH_TOKEN, expirableAccessToken.getRefreshCode());
                editor.putLong(DATE_ACQUIRED, System.currentTimeMillis());
            }
            editor.putString(TOKEN, token.getToken());
        } else {
            editor.putString(TOKEN, null);
            editor.putInt(EXPIRATION, -1);
            editor.putString(REFRESH_TOKEN, null);
            editor.putLong(DATE_ACQUIRED, -1);
        }

        editor.commit();
    }

    @Nullable
    public Map<Class, AccessToken> getTokens(){
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
        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        AccessToken accessToken = null;
        String token = sharedPref.getString(TOKEN, null);
        int expiration = sharedPref.getInt(EXPIRATION, -1);
        if(token != null || expiration != -1) {

            if (file.equals(GITHUB.getName())) {
                accessToken = new GitHubAccessToken();
            } else if (file.equals(BITBUCKET.getName())) {
                accessToken = new BitBucketAccessToken();
            }

            if(accessToken instanceof ExpirableAccessToken){
                ExpirableAccessToken expirableAccessToken = (ExpirableAccessToken) accessToken;
                expirableAccessToken.setRefreshToken(sharedPref.getString(REFRESH_TOKEN,null));

                long dateAcquired = sharedPref.getLong(DATE_ACQUIRED, -1);
                if(expiration != -1 && dateAcquired != -1){
                    expirableAccessToken.setExpiresIn(OAuthUtils.calcTimeToRefreshToken(dateAcquired, expiration));
                }
            }
            accessToken.setToken(token);
        }
        return accessToken;
    }

    /**
     * This method uses currentThread to write into SharedPreferences.
     *
     * @param file The shared prefs to write into.
     * @param owner The Owner to write into.
     */
   @SuppressLint("CommitPrefEdits")
   public void saveOwner(String file, @NonNull Owner owner){
        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USERNAME, owner.getLogin());
        editor.commit();
    }

    @Nullable
    Owner getOwner(@NonNull String file){
        Owner owner = null;
        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        String username = sharedPref.getString(USERNAME,null);
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
    public Map<Class, Owner> getOwners(){
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
