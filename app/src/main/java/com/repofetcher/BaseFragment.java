package com.repofetcher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.service.FetcherCallsHandler;

/**
 * Created by ricar on 07/09/2016.
 */
public abstract class BaseFragment extends Fragment implements FragmentTransitionService{

    private static final String TAG = BaseFragment.class.getName();

    @Nullable
    private FragmentTransitionService fragmentTransitionService;

    @LayoutRes protected int layoutRes;

    public BaseFragment(@LayoutRes int layoutRes){
        this.layoutRes = layoutRes;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutRes, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentTransitionService){
            fragmentTransitionService = (FragmentTransitionService)context;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentTransitionService = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FetcherCallsHandler.unSubscribe(this);
    }

    public void switchFragment(@NonNull Class<? extends BaseFragment> fragment, @Nullable Bundle bundle){
        if(fragmentTransitionService != null){
            fragmentTransitionService.switchFragment(fragment, bundle);
        }
    }

    public void goBack(){
        if(fragmentTransitionService != null){
            fragmentTransitionService.goBack();
        }
    }

    @Override
    public void goToLoginCenter() {
        if(fragmentTransitionService != null){
            fragmentTransitionService.goToLoginCenter();
        }
    }
}
