package com.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.service.holder.RepoServiceType;
import com.service.holder.ServiceHolderFactory;

/**
 * Created by ricar on 04/09/2016.
 */
public class ServiceUtils {

    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getBasicAuthorization(String firstParameter, String secondParameter){
        String credentials = firstParameter + ":" + secondParameter;
        return Constants.BASIC + " " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    public static int getHashCode(@NonNull Object object){
        return System.identityHashCode(object);
    }

    public static Class getServiceClass(@RepoServiceType int service){
        return new ServiceHolderFactory().create(service).getClass();
    }
}
