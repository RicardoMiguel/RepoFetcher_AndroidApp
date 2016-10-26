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
import com.service.holder.RepoServiceType;

/**
 * Created by ricar on 13/09/2016.
 */
public class LoginCenterFragment extends BaseFragment implements DialogInterface.OnMultiChoiceClickListener, LoginCenterContract.View {

    private static final String TAG = LoginCenterFragment.class.getName();

    private ViewGroup viewGroup;

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

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.floating_button);
        fab.setOnClickListener(v -> controller.createSessionsDialog());

        viewGroup = (ViewGroup)view.findViewById(R.id.sessions_layout);
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "OnDestroyView");
        super.onDestroyView();
    }

    @Override
    public void inflateSessionView(@NonNull String v) {
        Button button = (Button)LayoutInflater.from(this.getContext()).inflate(R.layout.repository_button, viewGroup, false);
        button.setText(v);
        viewGroup.addView(button);
    }

    @Override
    public void wipeSessionsView() {
        viewGroup.removeAllViews();
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
    public void showNoSessionsView() {
        //TODO Implement Errors view
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.activeSessions();
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
