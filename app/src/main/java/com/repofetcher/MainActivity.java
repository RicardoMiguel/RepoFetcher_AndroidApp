package com.repofetcher;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements FragmentTransitionService{

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        switchFragment(IntroFragment.class,null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void switchFragment(@NonNull Class<? extends BaseFragment> fragmentClass, @Nullable Bundle bundle) {
        Log.d(TAG, "onSwitchFragment");
        Fragment fragment = null;
        if(fragmentClass == IntroFragment.class){
            fragment = new IntroFragment();
        } else if(fragmentClass == MultipleAccountRepositoriesFragment.class){
            fragment = new MultipleAccountRepositoriesFragment();
        } else if(fragmentClass == LoginCenterFragment.class){
            fragment = new LoginCenterFragment();
        }

        if(fragment != null && bundle != null){
            fragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

    // Commit the transaction
        transaction.commit();
        Log.d("backstack",getSupportFragmentManager().getBackStackEntryCount()+"");
    }
}
