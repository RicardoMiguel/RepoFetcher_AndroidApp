package com.repofetcher;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.controller.ActionBarController;
import com.service.FetcherCallsHandler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentTransitionService{

    private static final String TAG = MainActivity.class.getName();

    private ActionBarController actionBarController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        actionBarController = new ActionBarController(getSupportActionBar(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        return actionBarController.onMainCreateOptionsMenu(menu, getMenuInflater());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarController.onMainOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        switchFragment(IntroFragment.class,null);
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
        } else if(fragmentClass == GitHubAccessTokenWebViewFragment.class){
            fragment = new GitHubAccessTokenWebViewFragment();
        } else if(fragmentClass == BitbucketAccessTokenWebViewFragment.class){
            fragment = new BitbucketAccessTokenWebViewFragment();
        }

        if(fragment != null && bundle != null){
            fragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void goToLoginCenter() {
        switchFragment(LoginCenterFragment.class, null);
    }

    public void searchRepositories(@Nullable String username) {
        if(!TextUtils.isEmpty(username)){
            Bundle bundle = new Bundle();
            bundle.putString( MultipleAccountRepositoriesFragment.TEXT, username);

            ArrayList<Integer> list = new ArrayList<>();
            list.add(FetcherCallsHandler.GITHUB);
            list.add(FetcherCallsHandler.BITBUCKET);
            bundle.putIntegerArrayList(MultipleAccountRepositoriesFragment.SERVICE_ALIAS, list);
            switchFragment(MultipleAccountRepositoriesFragment.class, bundle);
        }
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void setActionBar(BaseFragment baseFragment, Menu menu) {
        actionBarController.setActionBar(baseFragment, menu);
    }
}
