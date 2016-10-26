package com.controller;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.service.FetcherCallsHandler;
import com.service.holder.ServiceHolderFactory;

/**
 * Created by ricar on 24/10/2016.
 */

public class LoginCenterController implements LoginCenterContract.Controller{

    private LoginCenterContract.View view;
    private Resources resources;

    private int[] services;

    public LoginCenterController(@NonNull LoginCenterContract.View view, @NonNull Resources resources) {
        this.view = view;
        this.resources = resources;
        services = FetcherCallsHandler.getServicesAlias();
    }

    @Override
    public void activeSessions() {

        if(services.length == 0){
            view.showNoSessionsView();
        } else {
            for (int i = 0; i < services.length; i++) {
                if (FetcherCallsHandler.hasSession(services[i])) {
                    view.inflateSessionView(resources.getString(new ServiceHolderFactory().create(services[i]).getServiceName()));
                }
            }
        }

    }

    @Override
    public void createSessionsDialog() {
        String[] names = new String[services.length];
        boolean[] serviceHasSession = new boolean[services.length];
        for(int i = 0; i<services.length; i++){
            names[i] = resources.getString(new ServiceHolderFactory().create(services[i]).getServiceName());
            serviceHasSession[i] = FetcherCallsHandler.hasSession(services[i]);
        }

        view.showSessionsDialog(names, serviceHasSession);
    }

    @Override
    public void removeSession(int i) {
        FetcherCallsHandler.removeToken(services[i]);
        view.wipeSessionsView();
        activeSessions();
        view.dismissSessionsDialog();
    }

    @Override
    public void addSession(int i) {
        view.goToWebViewFragment(services[i]);
        view.dismissSessionsDialog();
    }
}
