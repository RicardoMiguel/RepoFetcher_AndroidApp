package com.controller;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.repofetcher.R;

import java.util.ArrayList;


/**
 * Created by ricar on 20/09/2016.
 */

public class FragmentController {

    private FragmentManager fragmentManager;

    private ArrayList<String> backStack;

    public FragmentController(@NonNull AppCompatActivity activity) {
        fragmentManager = activity.getSupportFragmentManager();
        backStack = new ArrayList<>();
    }

    public void saveBackStack(@Nullable Bundle outState){
        if(outState != null){
            outState.putStringArrayList(FragmentController.class.getName(), backStack);
//            Log.d("Fragment", backStack.toString());
        }
    }

    public void restoreState(@Nullable Bundle state){
        if(state != null) {
            backStack.addAll(state.getStringArrayList(FragmentController.class.getName()));
//            Log.d("Fragment", backStack.toString());
        }
    }

    public boolean hasFragments(){
        return !backStack.isEmpty();
    }

    private void pushToStack(@NonNull Fragment fragment){
        backStack.add(fragment.getClass().getName());
    }

    public void popFromStack(){
        backStack.remove(backStack.size()-1);
    }

    public int stackSize(){
        return backStack.size();
    }

    public void addToFragmentManager(@NonNull Fragment fragment, @Nullable Bundle bundle){
        if(bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getName());
        pushToStack(fragment);
        transaction.addToBackStack(fragment.getClass().getName());

        transaction.commit();
    }

    public boolean popBackStackImmediate(){

        return fragmentManager.popBackStackImmediate();
    }
}
