package com.onemt.h5boost

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.onemt.h5boost.preload.HttpClient
import com.onemt.h5boost.preload.WebViewPool
import com.tencent.sonic.sdk.SonicSession
import kotlinx.android.synthetic.main.browser_activity.*

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 14:27
 * @see
 */
class Browser2Activity: AppCompatActivity() {

    private var sonicSession: SonicSession? = null
    private var url = "https://www.taobao.com"
    private var start = System.currentTimeMillis()
    private val webView2 = WebViewPool.obtain()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.browser2_activity)
        fl_web.addView(webView2)
//        checkPermission()
        loadDefault(url)
        load.setOnClickListener {
            loadDefault(url)
        }
    }

    private fun loadDefault(url: String) {
        webView2.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("default", "${System.currentTimeMillis() - start}ms")
            }

            override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                if (url == this@Browser2Activity.url) {
                    return null
                }
                return super.shouldInterceptRequest(view, url)
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                return shouldInterceptRequest(view, request?.url?.toString())
            }
        }

        val webSettings = webView2.settings
        webSettings.javaScriptEnabled = true
        webView2.removeJavascriptInterface("searchBoxJavaBridge_")
        webSettings.allowContentAccess = true
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.savePassword = false
        webSettings.saveFormData = false
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true

        webView2.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        fl_web.removeView(webView2)
        WebViewPool.release(webView2)
    }

    private inner class Task: Runnable {
        override fun run() {
            val response = HttpClient.get(url).request()
            Log.e("response", response)
        }
    }
}