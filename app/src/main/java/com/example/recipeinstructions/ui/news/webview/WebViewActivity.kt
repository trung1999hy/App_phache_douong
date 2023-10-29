package com.example.recipeinstructions.ui.news.webview

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeinstructions.R

@Suppress("DEPRECATION")
class WebViewActivity : AppCompatActivity() {

    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_webview)

        webView = findViewById(R.id.WebView)
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                findViewById<ProgressBar>(R.id.ProgressLoadingGame).visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                findViewById<ProgressBar>(R.id.ProgressLoadingGame).visibility = View.GONE
                webView?.visibility = View.VISIBLE
            }

            @Deprecated("Deprecated in Java")
            override fun onReceivedError(
                mainWebView: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                findViewById<ProgressBar>(R.id.ProgressLoadingGame).visibility = View.GONE
                try {
                    mainWebView.stopLoading()
                    mainWebView.clearView()
                } catch (ignored: Exception) { }

                if (mainWebView.canGoBack()) {
                    mainWebView.goBack()
                }
                mainWebView.loadUrl("file:///android_asset/interneterror.html")

                val alertDialog = AlertDialog.Builder(this@WebViewActivity).create()
                val view: View =
                    LayoutInflater.from(this@WebViewActivity).inflate(R.layout.dialog_internet_error, null)
                alertDialog.setButton(
                    Dialog.BUTTON_POSITIVE, "Try Again"
                ) { _, _ ->
                    finish()
                    startActivity(intent)
                }

                alertDialog.setView(view)
                alertDialog.show()
                super.onReceivedError(mainWebView, errorCode, description, failingUrl)
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        webView?.loadUrl(intent?.extras?.getString("linkNews").toString())

        val webSettings: WebSettings = webView?.settings ?: return
        webSettings.javaScriptEnabled = true
        webView?.settings?.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webSettings.setEnableSmoothTransition(true)
    }

    @SuppressLint("InflateParams")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView?.canGoBack() ?: return) {
            webView?.goBack()
        } else {
            AlertDialog.Builder(this).apply {
                val view: View = LayoutInflater.from(this@WebViewActivity)
                    .inflate(R.layout.dialog_close_alert, null)
                setPositiveButton(
                    Html.fromHtml("<font color='#D800FF'>EXIT</font>")
                ) { _, _ -> finish() }
                setCancelable(false)
                setNegativeButton(
                    Html.fromHtml("<font color='#FF7F27'>NO</font>")
                ) { dialog, _ -> dialog.dismiss() }
                setView(view)
                show()
            }
        }
    }
}