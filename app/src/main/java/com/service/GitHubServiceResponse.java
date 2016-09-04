package com.service;

/**
 * Created by ricar on 02/09/2016.
 */
public interface GitHubServiceResponse<T>{
    void onSuccess(T object);
}
