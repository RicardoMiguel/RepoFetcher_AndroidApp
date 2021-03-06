package com.repofetcher;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.service.Constants;
import com.service.FetcherCallsHandler;
import com.service.holder.RepoServiceType;

public abstract class AccessTokenWebViewFragment extends BaseFragment{

    private static final String TAG = AccessTokenWebViewFragment.class.getName();

    protected WebView webView;

    protected String authorizationUrl;
    protected String clientId;
    protected String clientSecret;

    @Nullable String accessCode;

    public AccessTokenWebViewFragment() {
        super(R.layout.web_view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        authorizationUrl = FetcherCallsHandler.getAuthorizationUrl(getType());
        clientId = FetcherCallsHandler.getClientId(getType());
        clientSecret = FetcherCallsHandler.getClientSecret(getType());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = (WebView) view.findViewById(R.id.web_view);
        if(!TextUtils.isEmpty(authorizationUrl) && !TextUtils.isEmpty(clientId) && !TextUtils.isEmpty(clientSecret)) {
            configWebView();
        } else {
            goBack();
            //TODO ERROR MESSAGE
        }
    }

    protected void configWebView(){
        webView.getSettings().setJavaScriptEnabled(javaScriptEnable());

        //Required for running tests.
//        if (Build.VERSION.SDK_INT <= 18) {
//            webView.getSettings().setSavePassword(false);
//        }

        webView.loadUrl(getQuery());

        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                Uri uri = Uri.parse(url);
                String code = uri.getQueryParameter(Constants.CODE);
                if (!TextUtils.isEmpty(code)) {
                    webView.stopLoading();
                    accessCode = code;
                    exchangeCodeForToken(code);
                }
            }
        });
    }

    protected abstract String getQuery();

    protected abstract void exchangeCodeForToken(@NonNull String code);

    protected abstract @RepoServiceType int getType();

    protected abstract boolean javaScriptEnable();

    @Override
    public void retry() {
        if(!TextUtils.isEmpty(accessCode)){
            exchangeCodeForToken(accessCode);
        }
    }
}
