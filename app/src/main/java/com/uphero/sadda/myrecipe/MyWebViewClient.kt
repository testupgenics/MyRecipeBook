package com.uphero.sadda.myrecipe

import android.app.ProgressDialog
import android.webkit.WebView
import android.webkit.WebViewClient

class MyWebViewClient(private val progressDialog: ProgressDialog) : WebViewClient() {


    init {
        this.progressDialog.setMessage("Loading")
        this.progressDialog.setCancelable(false)
        this.progressDialog.show()
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        this.progressDialog.dismiss()
    }
}
