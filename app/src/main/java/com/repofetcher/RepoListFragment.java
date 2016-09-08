package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.controller.RepoListAdapter;
import com.controller.VerticalSpaceItemDecoration;
import com.model.Repo;
import com.model.bitbucket.BitBucketRepositories;
import com.model.github.GitHubRepo;
import com.service.FetcherCallsHandler;
import com.service.RepoServiceResponse;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;

/**
 * Created by ricar on 07/09/2016.
 */
public class RepoListFragment extends BaseFragment{

    private static final int REPO_LIST_ANIMATION_DURATION = 300;

    private RecyclerView repoListRecyclerView;
    private String user;
    private int repo;

    public RepoListFragment() {
        super(R.layout.repo_list);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(args != null){
            user = args.getString(IntroFragment.class.getName());
            repo = args.getInt(MultipleAccountRepositoriesFragment.class.getName());
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repoListRecyclerView = (RecyclerView)view.findViewById(R.id.repo_list_recycler_view);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!TextUtils.isEmpty(user)) {
            getRepoList(user, repo);
        }
    }

    private void buildRepositoriesRecyclerView(@NonNull List<? extends Repo> repoList){
        RepoListAdapter adapter = new RepoListAdapter(repoList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        repoListRecyclerView.setLayoutManager(mLayoutManager);
        repoListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        repoListRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(UIUtils.getDimenFromResources(getContext(), R.dimen.repo_list_vertical_space_decoration)));
        repoListRecyclerView.setAdapter(buildAnimations(adapter));
    }

    @NonNull
    private AnimationAdapter buildAnimations(@NonNull RepoListAdapter repoListAdapter){
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(repoListAdapter);
        alphaInAnimationAdapter.setDuration(REPO_LIST_ANIMATION_DURATION);
        alphaInAnimationAdapter.setFirstOnly(false);
        return alphaInAnimationAdapter;
    }

    private void getRepoList(@NonNull String user, int repo){
        switch (repo){
            case FetcherCallsHandler.GITHUB:
                getGitHubRepoList(user);
                break;
            case FetcherCallsHandler.BITBUCKET:
                getBitBucketRepoList(user);
                break;
        }
    }

    private void getBitBucketRepoList(@NonNull String user){
        FetcherCallsHandler.callListRepositories(FetcherCallsHandler.BITBUCKET, user, new RepoServiceResponse<BitBucketRepositories>() {

        @Override
        public void onSuccess(BitBucketRepositories object) {
            buildRepositoriesRecyclerView(object.getValues());
        }

        @Override
        public void onError(Throwable t) {
                Toast.makeText(RepoListFragment.this.getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.e("LOl","",t);
            }
        });
    }

    private void getGitHubRepoList(@NonNull String user){
        FetcherCallsHandler.callListRepositories(FetcherCallsHandler.GITHUB, user, new RepoServiceResponse<List<GitHubRepo>>() {
            @Override
            public void onSuccess(List<GitHubRepo> object) {
                buildRepositoriesRecyclerView(object);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(RepoListFragment.this.getContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
