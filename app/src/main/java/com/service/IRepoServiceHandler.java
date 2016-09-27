package com.service;

import android.support.annotation.NonNull;

import com.model.AccessToken;
import com.model.ExpirableAccessToken;
import com.model.Owner;
import com.service.request.ExchangeTokenRequest;
import com.service.request.GetOwnRepositoriesRequest;
import com.service.request.GetOwnerRequest;
import com.service.request.GetRepositoriesRequest;

/**
 * Created by ricar on 04/09/2016.
 */
public interface IRepoServiceHandler {
    <S> void callListRepositories(@NonNull GetRepositoriesRequest<S> request);
    <S> void callListRepositories(@NonNull GetOwnRepositoriesRequest<S> request);
    <S extends AccessToken> void exchangeToken(@NonNull ExchangeTokenRequest<S> request);
    <S extends ExpirableAccessToken> void refreshToken(@NonNull ExchangeTokenRequest<S> request);
    <S extends Owner> void callGetOwner(@NonNull GetOwnerRequest<S> request);
}
