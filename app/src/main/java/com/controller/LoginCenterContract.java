package com.controller;

import android.support.annotation.NonNull;

import com.service.holder.RepoServiceType;

/**
 * Created by ricar on 20/10/2016.
 */

public interface LoginCenterContract {

    interface View {
        void inflateSessionView(@NonNull String v);

        void wipeSessionsView();

        void showSessionsDialog(@NonNull String[] names, @NonNull boolean[] serviceHasSession);

        void dismissSessionsDialog();

        void goToWebViewFragment(@RepoServiceType int serviceType);

        void showNoSessionsView();
    }

    interface Controller {

        void activeSessions();

        void createSessionsDialog();

        void removeSession(int i);

        void addSession(int i);
    }


}
