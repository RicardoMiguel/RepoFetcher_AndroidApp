package com.controller;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.repofetcher.ErrorsContract;
import com.repofetcher.R;

/**
 * Created by ricar on 31/10/2016.
 */

public class ErrorUIController implements ErrorsContract.Controller {

    @ErrorType
    private int currentError;

    private ErrorsContract.View view;

    private View errorView;

    private TextView errorText;

    private ImageView errorImage;

    private Button errorButton;

    public ErrorUIController(@NonNull ErrorsContract.View view, @NonNull View errorView) {
        this.view = view;

        currentError = ErrorType.NETWORK_ERROR;
        this.errorView = errorView;
    }

    @Override
    public void createUnexpectedError() {
        inflateIfNotInflatedYet();
        if(currentError != ErrorType.UNEXPECTED_ERROR){
            errorText.setText(R.string.unexpected_error_label);
            errorImage.setImageResource(R.drawable.ic_error_black_48dp);
        }
        view.showErrorView();
    }

    @Override
    public void createNetworkError() {
        inflateIfNotInflatedYet();
        if(currentError != ErrorType.NETWORK_ERROR){
            errorText.setText(R.string.network_error_label);
            errorImage.setImageResource(R.drawable.ic_perm_scan_wifi_black_48dp);
        }
        view.showErrorView();
    }

    @Override
    public void handleError(Throwable t) {
        if(t instanceof NetworkErrorException){
            createNetworkError();
        } else {
            createUnexpectedError();
        }
    }

    private void inflateIfNotInflatedYet(){
        if(errorView instanceof ViewStub){
            errorView = ((ViewStub) errorView).inflate();
        }

        if(errorText == null){
            errorText = (TextView)errorView.findViewById(R.id.text_error);
            errorImage = (ImageView)errorView.findViewById(R.id.image_error);
            errorButton = (Button)errorView.findViewById(R.id.button_error);
            errorButton.setOnClickListener((view1)-> view.retry());
        }
    }
}
