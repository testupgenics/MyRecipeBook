package com.uphero.sadda.myrecipe;

import android.app.ProgressDialog;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Sadda on 10-Nov-17.
 */

public class MyWebViewClient extends WebViewClient {
    private ProgressDialog progressDialog;


    public MyWebViewClient(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
        this.progressDialog.setMessage("Loading");
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        this.progressDialog.dismiss();
    }
}
