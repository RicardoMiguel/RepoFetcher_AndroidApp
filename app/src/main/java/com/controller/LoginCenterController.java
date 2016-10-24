package com.controller;

import android.support.annotation.NonNull;

import com.service.FetcherCallsHandler;

/**
 * Created by ricar on 24/10/2016.
 */

public class LoginCenterController implements LoginCenterContract.Controller{

    private LoginCenterContract.View view;

    public LoginCenterController(@NonNull LoginCenterContract.View view){
        this.view = view;
    }

    @Override
    public void activeSessions() {
    }

    @Override
    public void createSessionsDialog() {

    }

    @Override
    public void removeSession(int i) {

    }

    @Override
    public void addSession(int i) {

    }
}
