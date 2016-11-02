package com.controller;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.repofetcher.ErrorsContract;
import com.repofetcher.R;

/**
 * Created by ricar on 02/11/2016.
 */

public class ErrorUIHandler implements ErrorsContract.Handler {

    private TextView errorText;

    private ImageView errorImage;

    private Button errorButton;

    private ErrorsContract.Controller controller;

    private View errorView;

    public ErrorUIHandler(@NonNull View errorView, @NonNull ErrorsContract.Controller controller) {
        this.errorView = errorView;
        this.controller = controller;
    }

    @Override
    public void createUnexpectedError() {
        inflateIfNotInflatedYet();
        errorText.setText(R.string.unexpected_error_label);
        errorImage.setImageResource(R.drawable.ic_error_black_48dp);
    }

    @Override
    public void createNetworkError() {
        inflateIfNotInflatedYet();
        errorText.setText(R.string.network_error_label);
        errorImage.setImageResource(R.drawable.ic_perm_scan_wifi_black_48dp);
    }

    private void inflateIfNotInflatedYet() {
        if (errorView instanceof ViewStub) {
            errorView = ((ViewStub) errorView).inflate();
            errorView.setVisibility(View.GONE);
        }

        if (errorText == null) {
            errorText = (TextView) errorView.findViewById(R.id.text_error);
            errorImage = (ImageView) errorView.findViewById(R.id.image_error);
            errorButton = (Button) errorView.findViewById(R.id.button_error);
            errorButton.setOnClickListener((view1) -> controller.onRetryButtonClick());
        }
    }
}
