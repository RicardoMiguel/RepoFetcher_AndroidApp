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

import com.model.github.GitHubAccessToken;
import com.service.Constants;
import com.service.FetcherCallsHandler;
import com.service.RepoServiceResponse;
import com.service.request.ExchangeTokenRequest;

/**
 * Created by ricar on 12/09/2016.
 */
public class WebViewFragment extends BaseFragment{

    private static final String TAG = WebViewFragment.class.getName();

    private WebView webView;

    private String authorizationUrl;
    private String clientId;
    private String clientSecret;
    @FetcherCallsHandler.RepoServiceType private int serviceType;

    public WebViewFragment() {
        super(R.layout.web_view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null && bundle.getSerializable(TAG) instanceof SerializableInteger){
            serviceType = ((SerializableInteger)bundle.getSerializable(TAG)).service;

            authorizationUrl = FetcherCallsHandler.getAuthorizationUrl(serviceType);
            clientId = FetcherCallsHandler.getClientId(serviceType);
            clientSecret = FetcherCallsHandler.getClientSecret(serviceType);
        }

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

    private void configWebView(){
        Uri url = Uri.parse(authorizationUrl)
                .buildUpon()
                .appendQueryParameter(Constants.CLIENT_ID, clientId)
                .appendQueryParameter(Constants.SCOPE, Constants.REPO)
                .build();
        webView.loadUrl(url.toString());

        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                Uri uri = Uri.parse(url);
                String code = uri.getQueryParameter(Constants.CODE);
                if (!TextUtils.isEmpty(code)) {
                    webView.stopLoading();
                    exchangeCodeForToken(code);
                }
            }
        });
    }

    private void exchangeCodeForToken(@NonNull String code){
        FetcherCallsHandler.callExchangeToken(serviceType, new ExchangeTokenRequest<>(this, code, clientId, clientSecret, new RepoServiceResponse<GitHubAccessToken>() {
            @Override
            public void onSuccess(GitHubAccessToken object) {
                goBack();
            }

            @Override
            public void onError(Throwable t) {
            }
        }));
    }
}
