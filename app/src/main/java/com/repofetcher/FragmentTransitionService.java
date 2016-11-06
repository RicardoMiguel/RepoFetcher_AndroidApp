package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;

public interface FragmentTransitionService {
    void switchFragment(@NonNull Class<? extends BaseFragment> fragment, @Nullable Bundle bundle);
    void goToLoginCenter();
    void goBack();
    boolean hasToBuildActionBar(@NonNull BaseFragment baseFragment);
    void setActionBar(@NonNull BaseFragment baseFragment, @NonNull Menu menu);
    void searchRepositories(@Nullable String username);
}
