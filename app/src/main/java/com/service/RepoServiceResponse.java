package com.service;

/**
 * Created by ricar on 02/09/2016.
 */
public interface RepoServiceResponse<T>{
    void onSuccess(T object);
    void onError(Throwable t);
}
