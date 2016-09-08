package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.controller.RepoListPageAdapter;
import com.service.FetcherCallsHandler;

/**
 * Created by ricar on 08/09/2016.
 */
public class MultipleAccountRepositoriesFragment extends BaseFragment{

    private String text;

    public MultipleAccountRepositoriesFragment() {
        super(R.layout.multiple_account_repos);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        text = bundle.getString(IntroFragment.class.getName(), null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TextView toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(text);

        //Dumb Data
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
