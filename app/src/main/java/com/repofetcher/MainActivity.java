package com.repofetcher;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.controller.RepoListAdapter;
import com.controller.VerticalSpaceItemDecoration;
import com.model.Repo;
import com.model.bitbucket.BitBucketRepositories;
import com.service.FetcherCallsHandler;
import com.service.RepoServiceResponse;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final int REPO_LIST_ANIMATION_DURATION = 300;

    private EditText usernameEditText;
    private RecyclerView repoListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        usernameEditText = (EditText)findViewById(R.id.username_edit_text);
        Button fetchRepoUserListButton = (Button)findViewById(R.id.fetch_repo_list_button);
        repoListRecyclerView = (RecyclerView)findViewById(R.id.repo_list_recycler_view);

        fetchRepoUserListButton.setOnClickListener( view -> getRepoList(usernameEditText.getText().toString()) );
    }

    private void getRepoList(@NonNull String user){
//        FetcherCallsHandler.callListRepositories(FetcherCallsHandler.GITHUB, user, new RepoServiceResponse<List<GitHubRepo>>() {
//            @Override
//            public void onSuccess(List<GitHubRepo> object) {
//                buildRepositoriesRecyclerView(object);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
//            }
//        });

        FetcherCallsHandler.callListRepositories(FetcherCallsHandler.BITBUCKET, user, new RepoServiceResponse<BitBucketRepositories>() {

            @Override
            public void onSuccess(BitBucketRepositories object) {
                buildRepositoriesRecyclerView(object.getValues());
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.e("LOl","",t);
            }
        });
    }

    private void buildRepositoriesRecyclerView(@NonNull List<? extends Repo> repoList){
        RepoListAdapter adapter = new RepoListAdapter(repoList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        repoListRecyclerView.setLayoutManager(mLayoutManager);
        repoListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        repoListRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(UIUtils.getDimenFromResources(this, R.dimen.repo_list_vertical_space_decoration)));
        repoListRecyclerView.setAdapter(buildAnimations(adapter));
    }

    @NonNull
    private AnimationAdapter buildAnimations(@NonNull RepoListAdapter repoListAdapter){
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(repoListAdapter);
        alphaInAnimationAdapter.setDuration(REPO_LIST_ANIMATION_DURATION);
        alphaInAnimationAdapter.setFirstOnly(false);
        return alphaInAnimationAdapter;
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
}
