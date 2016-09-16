package com.repofetcher;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.service.Constants;
import com.service.FetcherCallsHandler;

/**
 * Created by ricar on 16/09/2016.
 */
public class BitbucketAccessTokenWebViewFragment extends AccessTokenWebViewFragment {

    @Override
    protected String getQuery() {
        return Uri.parse(authorizationUrl)
                .buildUpon()
                .appendQueryParameter(Constants.CLIENT_ID, clientId)
                .appendQueryParameter(Constants.RESPONSE_TYPE, Constants.TOKEN)
                .build().toString();
    }

    protected void configWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getQuery());

        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                Uri uri = Uri.parse(url);
                String code = uri.getQueryParameter(Constants.ACCESS_TOKEN);
                if (!TextUtils.isEmpty(code)) {
                    webView.stopLoading();
                    exchangeCodeForToken(code);
                }
            }
        });
    }

    @Override
    protected void exchangeCodeForToken(@NonNull String code) {

    }

    @Override
    protected int getType() {
        return FetcherCallsHandler.BITBUCKET;
    }
}
