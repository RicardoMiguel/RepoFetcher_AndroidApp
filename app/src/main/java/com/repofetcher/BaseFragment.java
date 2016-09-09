package com.repofetcher;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ricar on 07/09/2016.
 */
public abstract class BaseFragment extends Fragment {

    protected FragmentTransitionService fragmentTransitionService;

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
    public void onDetach() {
        super.onDetach();
        fragmentTransitionService = null;
    }
}
