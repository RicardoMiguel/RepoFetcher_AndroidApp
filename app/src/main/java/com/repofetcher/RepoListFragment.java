package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.service.holder.RepoServiceType;
import com.service.request.GetOwnRepositoriesRequest;
import com.service.request.GetRepositoriesRequest;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;

/**
 * Created by ricar on 07/09/2016.
 */
public class RepoListFragment extends BaseFragment{

    private static final String TAG = RepoListFragment.class.getName();
    private static final int REPO_LIST_ANIMATION_DURATION = 300;
    private static final String REPOSITOES_LIST = "repositories_list";

    private RecyclerView repoListRecyclerView;
    private String user;
    private int repo;
    @Nullable private ArrayList<? extends Repo> repositoriesList;

    public RepoListFragment() {
        super(R.layout.repo_list);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        Bundle args = savedInstanceState != null ? savedInstanceState : getArguments();
        if(args != null){
            user = args.getString(MultipleAccountRepositoriesFragment.TEXT, null);
            repo = args.getInt(MultipleAccountRepositoriesFragment.SERVICE_ALIAS);
            repositoriesList = args.getParcelableArrayList(REPOSITOES_LIST);
        }
        super.onCreate(savedInstanceState);
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
        repoListRecyclerView = (RecyclerView)view.findViewById(R.id.repo_list_recycler_view);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        if(repositoriesList == null) {
            getRepoList(user, repo);
        } else {
            buildRepositoriesRecyclerView(repositoriesList);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putString(MultipleAccountRepositoriesFragment.TEXT, user);
        outState.putInt(MultipleAccountRepositoriesFragment.SERVICE_ALIAS, repo);
        if(repositoriesList != null){
            outState.putParcelableArrayList(REPOSITOES_LIST, repositoriesList);
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

    private void buildRepositoriesRecyclerView(@NonNull ArrayList<? extends Repo> repoList){
        if(!repoList.isEmpty()) {
            RepoListAdapter adapter = new RepoListAdapter(repoList, getContext());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            repoListRecyclerView.setLayoutManager(mLayoutManager);
            repoListRecyclerView.setItemAnimator(new DefaultItemAnimator());
            repoListRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(UIUtils.getDimenFromResources(getContext(), R.dimen.repo_list_vertical_space_decoration)));
            repoListRecyclerView.setAdapter(buildAnimations(adapter));
        }
        repositoriesList = repoList;
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
            case RepoServiceType.GITHUB:
                getGitHubRepoList(user);
                break;
            case RepoServiceType.BITBUCKET:
                getBitBucketRepoList(user);
                break;
        }
    }

    private void getBitBucketRepoList(@Nullable String user){
        if(user != null) {
            FetcherCallsHandler.callListRepositories(RepoServiceType.BITBUCKET, new GetRepositoriesRequest<>(this, user, new RepoServiceResponse<BitBucketRepositories>() {

                @Override
                public void onSuccess(BitBucketRepositories object) {
                    buildRepositoriesRecyclerView(object.getValues());
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(RepoListFragment.this.getContext(), t.toString(), Toast.LENGTH_LONG).show();
                }
            }));
        } else {
            FetcherCallsHandler.callListRepositories(RepoServiceType.BITBUCKET, new GetOwnRepositoriesRequest<>(this, new RepoServiceResponse<BitBucketRepositories>() {

                @Override
                public void onSuccess(BitBucketRepositories object) {
                    buildRepositoriesRecyclerView(object.getValues());
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(RepoListFragment.this.getContext(), t.toString(), Toast.LENGTH_LONG).show();
                }
            }));
        }
    }

    private void getGitHubRepoList(@Nullable String user){
        if(user != null) {
            FetcherCallsHandler.callListRepositories(RepoServiceType.GITHUB, new GetRepositoriesRequest<>(this, user, new RepoServiceResponse<ArrayList<GitHubRepo>>() {
                @Override
                public void onSuccess(ArrayList<GitHubRepo> object) {
                    buildRepositoriesRecyclerView(object);
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(RepoListFragment.this.getContext(), t.toString(), Toast.LENGTH_LONG).show();
                }
            }));
        } else {
            FetcherCallsHandler.callListRepositories(RepoServiceType.GITHUB, new GetOwnRepositoriesRequest<>(this, new RepoServiceResponse<ArrayList<GitHubRepo>>() {
                @Override
                public void onSuccess(ArrayList<GitHubRepo> object) {
                    buildRepositoriesRecyclerView(object);
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(RepoListFragment.this.getContext(), t.toString(), Toast.LENGTH_LONG).show();
                }
            }));
        }
    }
}
