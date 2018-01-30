package pk.encodersolutions.myrecipebook

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView

class WebActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        webView = findViewById(R.id.webView)
        webView.loadUrl(intent.getStringExtra("URL"))
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient(ProgressDialog(this))
    }
}
