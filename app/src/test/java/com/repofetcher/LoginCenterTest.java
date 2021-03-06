package com.repofetcher;

import android.content.res.Resources;

import com.controller.LoginCenterContract;
import com.controller.LoginCenterController;
import com.service.holder.RepoServiceType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class LoginCenterTest {

    @Mock
    private LoginCenterContract.View view;

    @Mock
    private Resources resources;

    private LoginCenterContract.Controller controller;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        controller = new LoginCenterController(view, resources);
    }

    @Test
    public void showNoSessionsView(){
        controller.activeSessions();

        verify(view).showNoSessionsView();
    }

    @Test
    public void addGitHubSessionAndOpensWebView(){
        controller.addSession(0);
        verify(view).goToWebViewFragment(RepoServiceType.GITHUB);
    }

    @Test
    public void addBitbucketSessionAndOpensWebView(){
        controller.addSession(1);

        verify(view).goToWebViewFragment(RepoServiceType.BITBUCKET);
    }


}
