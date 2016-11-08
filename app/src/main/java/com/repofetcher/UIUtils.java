package com.repofetcher;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.view.View;

public final class UIUtils {

    private UIUtils(){
    }

    public static int getDimenFromResources(@NonNull Context context, @DimenRes int resource){
        return (int)(context.getResources().getDimension(resource) / context.getResources().getDisplayMetrics().density);
    }

    public static void showView(View viewToShow, View... viewsToHide){
        for(View view : viewsToHide){
            view.setVisibility(View.GONE);
        }
        viewToShow.setVisibility(View.VISIBLE);
    }
}
