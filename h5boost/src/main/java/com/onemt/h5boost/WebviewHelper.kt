package com.onemt.h5boost

import android.os.Build
import android.webkit.WebView

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/22 19:41
 * @see
 */
object WebviewHelper {

    fun setUp(webView: WebView, jsInterface: JsInterface) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.removeJavascriptInterface("searchBoxJavaBridge_")
        webView.addJavascriptInterface(jsInterface, "sonic")
        webSettings.allowContentAccess = true
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.savePassword = false
        webSettings.saveFormData = false
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
    }
}