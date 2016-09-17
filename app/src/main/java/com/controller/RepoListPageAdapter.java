package com.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.repofetcher.MultipleAccountRepositoriesFragment;
import com.repofetcher.R;
import com.repofetcher.RepoListFragment;
import com.service.FetcherCallsHandler;

/**
 * Created by ricar on 08/09/2016.
 */
public class RepoListPageAdapter extends FragmentPagerAdapter {

    private Bundle[] repos;
    private Context context;

    public RepoListPageAdapter(Context context, FragmentManager fm, Bundle... repos) {
        super(fm);
        this.context = context;
        this.repos = repos;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int repo = repos[position].getInt(MultipleAccountRepositoriesFragment.SERVICE_ALIAS);
        switch (repo){
            case FetcherCallsHandler.GITHUB:
                return context.getResources().getString(R.string.github);
            case FetcherCallsHandler.BITBUCKET:
                return context.getResources().getString(R.string.bitbucket);
        }
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle dataHolder = repos[position];
        Fragment fragment = new RepoListFragment();
        fragment.setArguments(dataHolder);
        return fragment;
    }

    @Override
    public int getCount() {
        return repos.length;
    }
}
