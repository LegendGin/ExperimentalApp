package com.onemt.h5boost

import android.os.Bundle
import android.webkit.WebView
import com.tencent.sonic.sdk.SonicSessionClient
import java.util.HashMap

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 14:39
 * @see
 */
class SonicSessionClientImpl: SonicSessionClient() {

    private var webView: WebView? = null

    fun bindWebView(webView: WebView) {
        this.webView = webView
    }

    override fun loadDataWithBaseUrlAndHeader(
        baseUrl: String?,
        data: String?,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?,
        headers: HashMap<String, String>?
    ) {
        webView?.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
    }

    override fun loadUrl(url: String?, extraData: Bundle?) {
        webView?.loadUrl(url)
    }

    override fun loadDataWithBaseUrl(
        baseUrl: String?,
        data: String?,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?
    ) {
        webView?.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
    }


}