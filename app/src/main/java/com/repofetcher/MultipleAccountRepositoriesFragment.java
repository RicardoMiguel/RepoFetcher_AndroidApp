package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.controller.RepoListPageAdapter;

import java.util.ArrayList;

/**
 * Created by ricar on 08/09/2016.
 */
public class MultipleAccountRepositoriesFragment extends BaseFragment{

    public static final String SERVICE_ALIAS = "SERVICE_ALIAS";
    public static final String TEXT = "TEXT";

    private static final String TAG = MultipleAccountRepositoriesFragment.class.getName();
    @Nullable
    private String text;
    @Nullable
    private ArrayList<Integer> servicesAlias;

    public MultipleAccountRepositoriesFragment() {
        super(R.layout.multiple_account_repos);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        Bundle bundle = getArguments();
        if(bundle != null){
            text = bundle.getString(TEXT, null);
            servicesAlias = bundle.getIntegerArrayList(SERVICE_ALIAS);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);


        viewPager.setAdapter(new RepoListPageAdapter(this.getContext(), getChildFragmentManager(), workViewPagerData()));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
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
