package com.controller;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.repofetcher.ErrorsContract;
import com.repofetcher.R;

/**
 * Created by ricar on 31/10/2016.
 */

public class ErrorUIController implements ErrorsContract.controller{

    @ErrorType
    private int currentError;

    private View errorView;

    private TextView errorText;

    private ImageView errorImage;

    private Button errorButton;

    public ErrorUIController(@NonNull View errorView) {
        currentError = ErrorType.NETWORK_ERROR;
        this.errorView = errorView;
        errorText = (TextView)errorView.findViewById(R.id.text_error);
        errorImage = (ImageView)errorView.findViewById(R.id.image_error);
        errorButton = (Button)errorView.findViewById(R.id.button_error);
    }

    @Override
    public void createUnexpectedError() {
        if(currentError != ErrorType.UNEXPECTED_ERROR){
            errorText.setText(R.string.unexpected_error_label);
            errorImage.setImageResource(R.drawable.ic_error_black_48dp);
        }
    }

    @Override
    public void createNetworkError() {
        if(currentError != ErrorType.NETWORK_ERROR){
            errorText.setText(R.string.network_error_label);
            errorImage.setImageResource(R.drawable.ic_perm_scan_wifi_black_48dp);
        }
    }


}
