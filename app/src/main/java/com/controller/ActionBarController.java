package com.controller;

import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.repofetcher.AccessTokenWebViewFragment;
import com.repofetcher.BaseFragment;
import com.repofetcher.FragmentTransitionService;
import com.repofetcher.IntroFragment;
import com.repofetcher.LoginCenterFragment;
import com.repofetcher.R;
import com.repofetcher.RepoListFragment;

/**
 * Created by ricar on 19/09/2016.
 */
public class ActionBarController {
    private final ActionBar actionBar;
    private final FragmentTransitionService fragmentTransitionService;

    public ActionBarController(@NonNull ActionBar actionBar, @NonNull FragmentTransitionService fragmentTransitionService) {
        this.actionBar = actionBar;
        this.fragmentTransitionService = fragmentTransitionService;
    }

    public boolean onMainCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        menuInflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItem.collapseActionView();
                fragmentTransitionService.searchRepositories(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    public boolean onMainOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                fragmentTransitionService.goToLoginCenter();
                return true;
            case android.R.id.home:
                fragmentTransitionService.goBack();
                return true;
            default:
                return false;
        }
    }


    public void onFragmentCreateOptionsMenu(@NonNull BaseFragment baseFragment, @NonNull Menu menu) {

        menu.findItem(R.id.action_settings).setVisible(!(baseFragment instanceof LoginCenterFragment || baseFragment instanceof AccessTokenWebViewFragment));
        menu.findItem(R.id.action_search).setVisible(!(baseFragment instanceof AccessTokenWebViewFragment));

        actionBar.setDisplayHomeAsUpEnabled(!(baseFragment instanceof IntroFragment));
    }

    public boolean hasToBuildActionBar(@NonNull BaseFragment baseFragment){
        return !(baseFragment instanceof RepoListFragment);
    }
}
