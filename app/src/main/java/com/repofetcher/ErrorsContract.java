package com.repofetcher;

/**
 * Created by ricar on 31/10/2016.
 */

public interface ErrorsContract {
    interface View{
        void showNetworkErrorView();
        void showUnexpectedErrorView();
        void retry();
    }

    interface Handler{
        void createUnexpectedError();
        void createNetworkError();
    }

    interface Controller {
        void handleError(Throwable t);
        void onRetryButtonClick();
    }
}
