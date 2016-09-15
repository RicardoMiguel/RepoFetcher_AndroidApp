package com.service;

import android.support.annotation.NonNull;

import com.service.request.ExchangeTokenRequest;
import com.service.request.ListOwnRepositoriesRequest;
import com.service.request.ListRepositoriesRequest;

/**
 * Created by ricar on 04/09/2016.
 */
public interface IRepoServiceHandler {
    <S> void callListRepositories(@NonNull ListRepositoriesRequest<S> request);
    <S> void callListRepositories(@NonNull ListOwnRepositoriesRequest<S> request);
    <S> void exchangeToken(@NonNull ExchangeTokenRequest<S> request);

}
