package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.controller.RepoListPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricar on 08/09/2016.
 */
public class MultipleAccountRepositoriesFragment extends BaseFragment{

    public static final String SERVICE_ALIAS = "SERVICE_ALIAS";
    public static final String TEXT = "TEXT";

    private static final String TAG = MultipleAccountRepositoriesFragment.class.getName();
    @Nullable
    private String text;
    private ArrayList<Integer> servicesAlias;
    private List<RepoListFragment> fragments;

    public MultipleAccountRepositoriesFragment() {
        super(R.layout.multiple_account_repos);
        fragments = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
        if(bundle != null){
            text = bundle.getString(TEXT, null);
            servicesAlias = bundle.getIntegerArrayList(SERVICE_ALIAS);
            if(servicesAlias != null) {
                for (int i = 0; i < servicesAlias.size(); i++) {
                    Fragment fragment = getChildFragmentManager().getFragment(bundle, SERVICE_ALIAS + servicesAlias.get(i));
                    if(fragment instanceof RepoListFragment){
                        fragments.add(i, (RepoListFragment)fragment);
                    }
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);


        viewPager.setAdapter(new RepoListPageAdapter(getChildFragmentManager(), this.getContext(), fragments, workViewPagerData()));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putString(TEXT, text);
        outState.putIntegerArrayList(SERVICE_ALIAS, servicesAlias);
        for(int i = 0; i<servicesAlias.size();i++){
            RepoListFragment fragment = fragments.get(i);
            if(fragment != null){
                getChildFragmentManager().putFragment(outState, SERVICE_ALIAS + servicesAlias.get(i), fragment);
            }
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private Bundle[] workViewPagerData(){
        Bundle[] bundles = new Bundle[servicesAlias.size()];

        for(int i = 0; i<servicesAlias.size();i++){
            Bundle bundle = new Bundle();
            bundle.putInt(SERVICE_ALIAS, servicesAlias.get(i));
            if(!TextUtils.isEmpty(text)) {
                bundle.putString(TEXT, text);
            }
            bundles[i] = bundle;
        }

        return bundles;
    }
}
