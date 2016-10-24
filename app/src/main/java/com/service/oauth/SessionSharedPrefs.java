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
import com.service.Constants;
import com.service.ServiceUtils;
import com.service.holder.RepoServiceType;

import java.util.HashMap;
import java.util.Map;

/**
 * This class uses currentThread to write into SharedPreferences.
 *
 * Created by ricar on 15/09/2016.
 */
public class SessionSharedPrefs {

    private static final String TOKEN = Constants.TOKEN;

    private static final String EXPIRATION = "expiration";

    private static final String REFRESH_TOKEN = Constants.REFRESH_TOKEN;

    private static final String DATE_ACQUIRED = "date_acquired";

    private static final String USERNAME = Constants.USERNAME;

    private Context context;

    public SessionSharedPrefs(@NonNull Context context){
        this.context = context;
    }

    /**
     * This method uses currentThread to write into SharedPreferences.
     *
     * @param serviceType The service tye to write into.
     * @param token The AccessToken to write into.
     */
    @SuppressLint("CommitPrefEdits")
    public void saveToken(@RepoServiceType int serviceType, @Nullable AccessToken token){
        final String file = ServiceUtils.getServiceClass(serviceType).getName();

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
    public Map<Integer, AccessToken> getTokens(@RepoServiceType int[] services){
        Map<Integer, AccessToken> map = null;

        for(Integer c : services){
            AccessToken token = getToken(c);
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
    AccessToken getToken(@RepoServiceType int serviceType){
        final String file = ServiceUtils.getServiceClass(serviceType).getName();

        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        AccessToken accessToken = null;
        String token = sharedPref.getString(TOKEN, null);
        int expiration = sharedPref.getInt(EXPIRATION, -1);
        if(token != null || expiration != -1) {

            if (serviceType == RepoServiceType.GITHUB) {
                accessToken = new GitHubAccessToken();
            } else if (serviceType == RepoServiceType.BITBUCKET) {
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
     * @param serviceType The service tye to write into.
     * @param owner The Owner to write into.
     */
   @SuppressLint("CommitPrefEdits")
   public void saveOwner(@RepoServiceType int serviceType, @NonNull Owner owner){
        final String file = ServiceUtils.getServiceClass(serviceType).getName();

        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USERNAME, owner.getLogin());
        editor.commit();
    }

    @Nullable
    Owner getOwner(@RepoServiceType int serviceType){
        final String file = ServiceUtils.getServiceClass(serviceType).getName();

        Owner owner = null;
        SharedPreferences sharedPref = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        String username = sharedPref.getString(USERNAME,null);
        if(username != null){
            if(serviceType == RepoServiceType.GITHUB){
                owner = new GitHubOwner();
            } else if(serviceType == RepoServiceType.BITBUCKET){
                owner = new BitBucketOwner();
            }
            owner.setLogin(username);
        }
        return owner;
    }

    @Nullable
    public Map<Integer, Owner> getOwners(@RepoServiceType int[] services){
        Map<Integer, Owner> owners = null;

        for(Integer c : services){
            Owner owner = getOwner(c);
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
