package com.service.request;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.service.RepoServiceResponse;

public class InitRequest extends BaseRequest<Void>{

    public InitRequest(@NonNull Object context, @Nullable RepoServiceResponse<Void> uiServiceResponse) {
        super(context, uiServiceResponse);
    }

}
