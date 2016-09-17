package com.service.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.model.Owner;
import com.service.RepoServiceResponse;

/**
 * Created by ricar on 17/09/2016.
 */
public class GetOwnerRequest<T extends Owner> extends BaseRequest<T> {

    public GetOwnerRequest(@NonNull Fragment context, @Nullable RepoServiceResponse<T> response) {
        setHash(context);
        setUiServiceResponse(response);
    }

    public GetOwnerRequest(int hash, @Nullable RepoServiceResponse<T> response) {
        setHash(hash);
        setUiServiceResponse(response);
    }
}
