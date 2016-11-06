package com.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.repofetcher.MultipleAccountRepositoriesFragment;
import com.repofetcher.R;
import com.repofetcher.RepoListFragment;
import com.service.holder.RepoServiceType;

import java.util.List;

public class RepoListPageAdapter extends FragmentPagerAdapter {

    private Bundle[] repos;
    private List<RepoListFragment> fragments;
    private Context context;

    public RepoListPageAdapter(FragmentManager fm, @NonNull Context context, @NonNull List<RepoListFragment> fragments, @NonNull Bundle[] repos) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.repos = repos;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int repo = repos[position].getInt(MultipleAccountRepositoriesFragment.SERVICE_ALIAS);
        switch (repo){
            case RepoServiceType.GITHUB:
                return context.getResources().getString(R.string.github);
            case RepoServiceType.BITBUCKET:
                return context.getResources().getString(R.string.bitbucket);
        }
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        RepoListFragment repoListFragment = null;
        if(position < fragments.size()){
            repoListFragment = fragments.get(position);
        }
        Bundle dataHolder = repos[position];

        if(repoListFragment == null){
            repoListFragment = new RepoListFragment();
            fragments.add(position, repoListFragment);
        }

        repoListFragment.setArguments(dataHolder);
        return repoListFragment;
    }

    @Override
    public int getCount() {
        return repos.length;
    }
}
