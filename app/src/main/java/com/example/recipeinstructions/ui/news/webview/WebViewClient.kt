package com.example.recipeinstructions.ui.news.webview

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient

@Suppress("DEPRECATION")
open class WebViewClient : WebViewClient() {
    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun onReceivedError(
        mainWebView: WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        try {
            mainWebView.stopLoading()
        } catch (ignored: Exception) {
        }
        try {
            mainWebView.clearView()
        } catch (ignored: Exception) {
        }
        if (mainWebView.canGoBack()) {
            mainWebView.goBack()
        }

        super.onReceivedError(mainWebView, errorCode, description, failingUrl)
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
    }

//    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {}

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return super.shouldOverrideUrlLoading(view, url)
    }
}