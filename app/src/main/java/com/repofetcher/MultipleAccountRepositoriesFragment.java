package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.controller.RepoListPageAdapter;
import com.service.FetcherCallsHandler;

/**
 * Created by ricar on 08/09/2016.
 */
public class MultipleAccountRepositoriesFragment extends BaseFragment{

    public MultipleAccountRepositoriesFragment() {
        super(R.layout.multiple_account_repos);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        //Holder Data
        Bundle github = new Bundle();
        github.putString(IntroFragment.class.getName(), "RicardoMiguel");
        github.putInt(MultipleAccountRepositoriesFragment.class.getName(), FetcherCallsHandler.GITHUB);

        Bundle bitbucket = new Bundle();
        bitbucket.putString(IntroFragment.class.getName(), "1111114_ricardo");
        bitbucket.putInt(MultipleAccountRepositoriesFragment.class.getName(), FetcherCallsHandler.BITBUCKET);
        //

        viewPager.setAdapter(new RepoListPageAdapter(this.getContext(), getFragmentManager(), github, bitbucket));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
