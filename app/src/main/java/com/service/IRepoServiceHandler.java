package com.service;

import android.support.annotation.NonNull;

import com.model.AccessToken;
import com.service.request.ExchangeTokenRequest;
import com.service.request.GetOwnRepositoriesRequest;
import com.service.request.GetRepositoriesRequest;

/**
 * Created by ricar on 04/09/2016.
 */
public interface IRepoServiceHandler {
    <S> void callListRepositories(@NonNull GetRepositoriesRequest<S> request);
    <S> void callListRepositories(@NonNull GetOwnRepositoriesRequest<S> request);
    <S extends AccessToken> void exchangeToken(@NonNull ExchangeTokenRequest<S> request);
}
