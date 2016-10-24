package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.service.FetcherCallsHandler;
import com.service.holder.RepoServiceType;

/**
 * Created by ricar on 13/09/2016.
 */
public class LoginCenterFragment extends BaseFragment{

    private static final String TAG = LoginCenterFragment.class.getName();

    private Button gitHubButton;
    private Button bitbucketButton;

    public LoginCenterFragment() {
        super(R.layout.login_center);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        gitHubButton = (Button)view.findViewById(R.id.login_github_button);
        gitHubButton.setOnClickListener( v -> goToWebViewFragment(RepoServiceType.GITHUB));

        bitbucketButton = (Button)view.findViewById(R.id.login_bitbucket_button);
        bitbucketButton.setOnClickListener(v -> goToWebViewFragment(RepoServiceType.BITBUCKET));
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "OnDestroyView");
        super.onDestroyView();
    }

    private void goToWebViewFragment(@RepoServiceType int serviceType) {
        switch (serviceType){
            case RepoServiceType.GITHUB:
                switchFragment(GitHubAccessTokenWebViewFragment.class,null);
                break;
            case RepoServiceType.BITBUCKET:
                switchFragment(BitbucketAccessTokenWebViewFragment.class,null);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setGitHubButton();
        setBitBucketButton();
    }

    private void setBitBucketButton() {
        if(FetcherCallsHandler.hasSession(RepoServiceType.BITBUCKET)){
            bitbucketButton.setEnabled(false);
            bitbucketButton.setText(R.string.logged_in_bitbucket_label);
        } else {
            bitbucketButton.setEnabled(true);
            bitbucketButton.setText(R.string.login_to_bitbucket_label);
        }
    }

    private void setGitHubButton() {
        if(FetcherCallsHandler.hasSession(RepoServiceType.GITHUB)){
            gitHubButton.setEnabled(false);
            gitHubButton.setText(R.string.logged_in_github_label);
        } else {
            gitHubButton.setEnabled(true);
            gitHubButton.setText(R.string.login_to_github_label);
        }
    }
}
