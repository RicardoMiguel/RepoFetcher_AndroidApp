package com.repofetcher;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ricar on 12/09/2016.
 */
public class WebViewFragment extends BaseFragment{

    private static final String TAG = WebViewFragment.class.getName();

    private WebView webView;

    public WebViewFragment() {
        super(R.layout.web_view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = (WebView) view.findViewById(R.id.web_view);
        webView.loadUrl("https://github.com/login/oauth/authorize?client_id=6ad10b7063dcf95b3eaa");

        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Is this the callback url?

                Uri uri= Uri.parse(url);
                String code = uri.getQueryParameter("code");
                if(!TextUtils.isEmpty(code)){
                    webView.stopLoading();
                    exchangeCodeForToken(code);
                }
            }
        });
    }

    private void exchangeCodeForToken(@NonNull String code){
        Log.d(TAG, code);
    }
}
