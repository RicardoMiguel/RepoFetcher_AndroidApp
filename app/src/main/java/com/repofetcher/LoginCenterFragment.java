package com.repofetcher;

import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import com.service.FetcherCallsHandler;
import com.service.holder.RepoServiceType;

/**
 * Created by ricar on 13/09/2016.
 */
public class LoginCenterFragment extends BaseFragment implements DialogInterface.OnMultiChoiceClickListener {

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
        bitbucketButton.setOnClickListener(v -> goToWebViewFragment(FetcherCallsHandler.BITBUCKET));

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.floating_button);
        fab.setOnClickListener(v -> createDialog());
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.chose_account_label).setMultiChoiceItems(R.array.repositories_names,
                new boolean[]{FetcherCallsHandler.hasSession(FetcherCallsHandler.GITHUB), FetcherCallsHandler.hasSession(FetcherCallsHandler.BITBUCKET)},
                this);
        AppCompatDialog alert = builder.create();
        alert.show();
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
            gitHubButton.setEnabled(false);
            gitHubButton.setText(R.string.github);
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
        String [] repoServices = getResources().getStringArray(R.array.repositories_names);
        final String github = getString(R.string.github);
        final String bitbucket = getString(R.string.bitbucket);
        if(b) {
            if (repoServices[i].equals(github)) {
                goToWebViewFragment(FetcherCallsHandler.GITHUB);
            } else if (repoServices[i].equals(bitbucket)) {
                goToWebViewFragment(FetcherCallsHandler.BITBUCKET);
            }
        } else {

        }
        dialogInterface.dismiss();
    }
}
