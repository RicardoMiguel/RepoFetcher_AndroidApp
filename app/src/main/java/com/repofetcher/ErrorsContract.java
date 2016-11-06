package com.repofetcher;

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
