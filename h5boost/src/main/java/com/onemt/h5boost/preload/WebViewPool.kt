package com.onemt.h5boost.preload

import android.content.Context
import android.support.v4.util.Pools
import android.util.Log
import android.webkit.WebView

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/21 9:21
 * @see
 */
object WebViewPool {

    private var webViewPools = Pools.SimplePool<WebView>(1)
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
        /*var webView = webViewPools.acquire()
        if (webView == null) {
            webView = WebView(context)
        }*/
        webViewPools.release(WebView(context))
    }

    fun obtain(): WebView {
        var webView = webViewPools.acquire()
        if (webView == null) {
            webView = WebView(context)
        } else {
            Log.e("obtain", "obtain from pool")
        }
        return webView
    }

    fun release(webView: WebView) {
        webView.loadUrl("")
        webViewPools.release(webView)
    }
}