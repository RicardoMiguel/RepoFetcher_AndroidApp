package com.repofetcher;

import android.accounts.NetworkErrorException;

import com.controller.ErrorUIController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class GenericErrorsTest {

    @Mock
    private ErrorsContract.View view;

    private ErrorsContract.Controller controller;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        controller = new ErrorUIController(view);
    }

    @Test
    public void showNetworkError(){
        controller.handleError(new NetworkErrorException());

        verify(view).showNetworkErrorView();
    }

    @Test
    public void showUnexpectedError(){
        controller.handleError(new IllegalAccessException());

        verify(view).showUnexpectedErrorView();
    }

    @Test
    public void retryClick(){
        controller.onRetryButtonClick();

        verify(view).retry();
    }
}
