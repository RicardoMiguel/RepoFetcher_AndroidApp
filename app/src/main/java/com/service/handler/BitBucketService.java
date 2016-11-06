package com.service.handler;

import com.model.bitbucket.BitBucketAccessToken;
import com.model.bitbucket.BitBucketOwner;
import com.model.bitbucket.BitBucketRepositories;
import com.service.Constants;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

interface BitBucketService {
    @GET("repositories/{user}/")
    Observable<BitBucketRepositories> listRepositories(@Path("user") String user);

    @FormUrlEncoded
    @POST
    Observable<BitBucketAccessToken> exchangeToken(@Url String url, @Header(Constants.AUTHORIZATION) String basic, @Field("grant_type") String grant, @Field(Constants.CODE)String code);

    @FormUrlEncoded
    @POST
    Observable<BitBucketAccessToken> refreshToken(@Url String url, @Header(Constants.AUTHORIZATION) String basic, @Field("grant_type") String grant, @Field("refresh_token")String code_refresh);

    @GET("user")
    Observable<BitBucketOwner> getOwner();
}
