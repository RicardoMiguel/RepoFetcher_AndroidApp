package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.service.FetcherCallsHandler;

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
        gitHubButton.setOnClickListener( v -> goToWebViewFragment(FetcherCallsHandler.GITHUB));
    }

    private void goToWebViewFragment(@FetcherCallsHandler.RepoServiceType int serviceType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(WebViewFragment.class.getName(), new SerializableInteger(serviceType));
        switchFragment(WebViewFragment.class, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        setGitHubButton();
    }

    private void setGitHubButton() {
    }
}
