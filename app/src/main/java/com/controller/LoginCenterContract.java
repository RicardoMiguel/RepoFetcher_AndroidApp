package com.controller;

import android.support.annotation.NonNull;

import com.service.FetcherCallsHandler;

/**
 * Created by ricar on 20/10/2016.
 */

public interface LoginCenterContract {

    interface View {
        void inject(@NonNull String v);

        void showSessionsDialog();

        void dismissSessionsDialog();

        void goToWebViewFragment(@FetcherCallsHandler.RepoServiceType int serviceType);
    }

    interface Controller {

        void activeSessions();

        void createSessionsDialog();

        void removeSession(int i);

        void addSession(int i);
    }


}
