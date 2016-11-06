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
import android.view.ViewStub;

import com.controller.ErrorUIController;
import com.controller.ErrorUIHandler;
import com.service.FetcherCallsHandler;

import static com.repofetcher.UIUtils.showView;

public abstract class BaseFragment extends Fragment implements ErrorsContract.View {

    private static final String TAG = BaseFragment.class.getName();

    @Nullable
    private FragmentTransitionService fragmentTransitionService;

    @LayoutRes protected int layoutRes;

    private View mainContent;

    private View errorContent;

    protected ErrorsContract.Controller errorController;

    protected ErrorsContract.Handler errorHandler;

    public BaseFragment(@LayoutRes int layoutRes){
        this.layoutRes = layoutRes;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentTransitionService){
            fragmentTransitionService = (FragmentTransitionService)context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(fragmentTransitionService != null && fragmentTransitionService.hasToBuildActionBar(this));
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewStub mainContentStub = (ViewStub)view.findViewById(R.id.main_content);
        mainContentStub.setLayoutResource(layoutRes);
        mainContentStub.setOnInflateListener((viewStub, view1) -> mainContent = view1);
        mainContentStub.inflate();

        errorContent = view.findViewById(R.id.error_content);
        errorController = new ErrorUIController(this);
        errorHandler = new ErrorUIHandler(errorContent, errorController);

        ViewStub errorContentStub = (ViewStub)errorContent;
        errorContentStub.setLayoutResource(R.layout.error_view);
        errorContentStub.setOnInflateListener((viewStub, view1) -> errorContent = view1);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        if(fragmentTransitionService != null){
            fragmentTransitionService.setActionBar(this, menu);
        }
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

    public void goToLoginCenter() {
        if(fragmentTransitionService != null){
            fragmentTransitionService.goToLoginCenter();
        }
    }

    public void showContentView(){
        if(mainContent.getVisibility() != View.VISIBLE) {
            showView(mainContent, errorContent);
        }
    }

    public void showErrorView(){
        if(errorContent.getVisibility() != View.VISIBLE) {
            showView(errorContent, mainContent);
        }
    }

    public void showNetworkErrorView(){
        errorHandler.createNetworkError();
        showErrorView();
    }

    public void showUnexpectedErrorView(){
        errorHandler.createUnexpectedError();
        showErrorView();
    }

    public void retry(){}
}
