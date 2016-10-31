package com.repofetcher;

/**
 * Created by ricar on 31/10/2016.
 */

public interface ErrorsContract {
    interface View{
        void showUnexpectedError();
        void showNetworkError();
    }

    interface controller{
        void createUnexpectedError();
        void createNetworkError();
    }
}
