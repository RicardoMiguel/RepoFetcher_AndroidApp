package com.repofetcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.service.FetcherCallsHandler;
import com.service.RepoServiceResponse;
import com.service.request.InitRequest;

public class SplashScreenActivity extends AppCompatActivity{

    @Override
    protected void onResume() {
        super.onResume();
        FetcherCallsHandler.load(new InitRequest(this, new RepoServiceResponse<Void>() {
            @Override
            public void onSuccess(Void object) {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable t) {
                //Intentional empty method.
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FetcherCallsHandler.unSubscribe(this);
    }
}
