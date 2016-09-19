package com.repofetcher;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.service.FetcherCallsHandler;

import java.util.ArrayList;

/**
 * Created by ricar on 07/09/2016.
 */
public class IntroFragment extends BaseFragment{
    private static final String TAG = IntroFragment.class.getName();

    public IntroFragment() {
        super(R.layout.intro);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.login_center_button).setOnClickListener( v -> goToLoginCenter());

        View myRepositoriesButton = view.findViewById(R.id.my_repositories_button);
        if(FetcherCallsHandler.hasSessions()){
            myRepositoriesButton.setVisibility(View.VISIBLE);
            myRepositoriesButton.setOnClickListener( v -> goToMyRepositories());
        } else {
            myRepositoriesButton.setVisibility(View.GONE);
        }
    }

    private void goToMyRepositories() {
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(MultipleAccountRepositoriesFragment.SERVICE_ALIAS, FetcherCallsHandler.getSessionsServicesAlias());
        switchFragment(MultipleAccountRepositoriesFragment.class, bundle);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
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

}
