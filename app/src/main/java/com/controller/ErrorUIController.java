package com.controller;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import com.repofetcher.ErrorsContract;

public class ErrorUIController implements ErrorsContract.Controller {

    private ErrorsContract.View view;

    public ErrorUIController(@NonNull ErrorsContract.View view) {
        this.view = view;
    }

    @Override
    public void handleError(Throwable t) {
        if(t instanceof NetworkErrorException){
            view.showNetworkErrorView();
        } else {
            view.showUnexpectedErrorView();
        }
    }

    @Override
    public void onRetryButtonClick() {
        view.retry();
    }
}
