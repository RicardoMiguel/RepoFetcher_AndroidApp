package com.repofetcher;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.controller.LoginCenterContract;
import com.controller.LoginCenterController;
import com.service.FetcherCallsHandler;
import com.service.holder.RepoServiceType;

/**
 * Created by ricar on 13/09/2016.
 */
public class LoginCenterFragment extends BaseFragment implements DialogInterface.OnMultiChoiceClickListener, LoginCenterContract.View {

    private static final String TAG = LoginCenterFragment.class.getName();

    private Button gitHubButton;
    private Button bitbucketButton;

    @Nullable
    private AppCompatDialog alert;

    private LoginCenterContract.Controller controller;

    public LoginCenterFragment() {
        super(R.layout.login_center);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        controller = new LoginCenterController(this, this.getResources());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        gitHubButton = (Button)view.findViewById(R.id.login_github_button);

        bitbucketButton = (Button)view.findViewById(R.id.login_bitbucket_button);

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.floating_button);
        fab.setOnClickListener(v -> controller.createSessionsDialog());
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "OnDestroyView");
        super.onDestroyView();
    }

    @Override
    public void inject(@NonNull String v) {

    }

    @Override
    public void showSessionsDialog(@NonNull String[] names, @NonNull boolean[] serviceHasSession) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.chose_account_label).setMultiChoiceItems(names,
                serviceHasSession,
                this);
        alert = builder.create();
        alert.show();
    }

    @Override
    public void dismissSessionsDialog() {
        if(alert != null){
            alert.dismiss();
            alert = null;
        }
    }

    public void goToWebViewFragment(@RepoServiceType int serviceType) {
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
            gitHubButton.setEnabled(false);
            gitHubButton.setText(R.string.github);
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
        if(b){
            controller.addSession(i);
        } else {
            controller.removeSession(i);
        }
    }
}
