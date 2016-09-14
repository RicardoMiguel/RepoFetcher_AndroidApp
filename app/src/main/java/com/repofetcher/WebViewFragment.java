package com.repofetcher;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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

import org.w3c.dom.Text;

/**
 * Created by ricar on 12/09/2016.
 */
public class WebViewFragment extends BaseFragment{

    private static final String TAG = WebViewFragment.class.getName();

    public static final String AUTHORIZATION_URL = "AUTHORIZATION_URL";
    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String CLIENT_SECRET = "CLIENT_SECRET";

    private WebView webView;

    private String authorizationUrl;
    private String clientId;
    private String clientSecret;

    public WebViewFragment() {
        super(R.layout.web_view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            authorizationUrl = bundle.getString(AUTHORIZATION_URL);
            clientId = bundle.getString(CLIENT_ID);
            clientSecret = bundle.getString(CLIENT_SECRET);
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
        FetcherCallsHandler.callExchangeToken(new ExchangeTokenRequest<>(this, code, clientId, clientSecret, new RepoServiceResponse<GitHubAccessToken>() {
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
