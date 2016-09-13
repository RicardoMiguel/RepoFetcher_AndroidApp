package com.repofetcher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by ricar on 07/09/2016.
 */
public interface FragmentTransitionService {
    void switchFragment(@NonNull Class<? extends BaseFragment> fragment, @Nullable Bundle bundle);
    void goBack();
}
