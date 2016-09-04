package com.repofetcher;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;

/**
 * Created by ricar on 03/09/2016.
 */
public class UIUtils {
    public static int getDimenFromResources(@NonNull Context context, @DimenRes int resource){
        return (int)(context.getResources().getDimension(resource) / context.getResources().getDisplayMetrics().density);
    }
}
