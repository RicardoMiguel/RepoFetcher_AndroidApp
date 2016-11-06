package com.service;

public interface RepoServiceResponse<T>{
    void onSuccess(T object);
    void onError(Throwable t);
}
