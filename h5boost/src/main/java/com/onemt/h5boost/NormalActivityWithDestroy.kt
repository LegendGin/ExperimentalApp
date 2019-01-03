package com.onemt.h5boost

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.onemt.h5boost.preload.HttpClient
import kotlinx.android.synthetic.main.normal_activity.*

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 14:27
 * @see
 */
class NormalActivityWithDestroy: AppCompatActivity() {

    private var url = MainActivity.url
    private var start = System.currentTimeMillis()
    private var startSendRequest = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.normal_activity)
        loadDefault(url)
        load.setOnClickListener {
            start = System.currentTimeMillis()
            webView2.loadUrl(url)
        }
    }

    private fun loadDefault(url: String) {
        webView2.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("onPageFinished", "${System.currentTimeMillis() - start}ms")
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                return shouldInterceptRequest(view, request?.url?.toString())
            }

            override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                if (!startSendRequest) {
                    Log.e("shouldInterceptRequest", "send request after ${System.currentTimeMillis() - start}ms")
                    startSendRequest = true
                }
                return super.shouldInterceptRequest(view, url)
            }
        }
        WebviewHelper.setUp(webView2, JsInterface(start, tv_time))

        webView2.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        webView2.clearCache(true)
        webView2.destroy()
    }
}