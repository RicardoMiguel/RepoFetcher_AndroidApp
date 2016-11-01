package com.repofetcher;

/**
 * Created by ricar on 31/10/2016.
 */

public interface ErrorsContract {
    interface View{
        void showErrorView();
        void retry();
    }

    interface Controller {
        void createUnexpectedError();
        void createNetworkError();
        void handleError(Throwable t);
    }
}
