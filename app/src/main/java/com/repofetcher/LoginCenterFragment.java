package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by ricar on 13/09/2016.
 */
public class LoginCenterFragment extends BaseFragment{

    private Button gitHubButton;

    public LoginCenterFragment() {
        super(R.layout.login_center);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gitHubButton = (Button)view.findViewById(R.id.login_github_button);
        gitHubButton.setOnClickListener( v -> goToWebViewFragment());
    }

    private void goToWebViewFragment() {
        switchFragment(WebViewFragment.class, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        setGitHubButton();
    }

    private void setGitHubButton() {
    }
}
