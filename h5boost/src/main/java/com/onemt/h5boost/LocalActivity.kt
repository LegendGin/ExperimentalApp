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
import com.onemt.h5boost.preload.H5Engine
import com.onemt.h5boost.preload.Session
import com.tencent.sonic.sdk.SonicUtils
import kotlinx.android.synthetic.main.normal_activity.*
import java.io.File
import java.io.FileInputStream

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 14:27
 * @see
 */
class LocalActivity: AppCompatActivity() {

    private var url = MainActivity.url
    private var start = System.currentTimeMillis()

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

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                return shouldInterceptRequest(view, request?.url?.toString())
            }

            override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                Log.e("shouldInterceptRequest", "url:$url")
                val file = File("$cacheDir${File.separator}${SonicUtils.getMD5(url)}")
                return if (file.exists()) {
                    Log.e("shouldInterceptRequest", "has Cache")
                    WebResourceResponse("text/html", "utf-8", FileInputStream(file))
                } else {
                    Log.e("shouldInterceptRequest", "no Cache")
                    null
                }
            }
        }

        WebviewHelper.setUp(webView2, JsInterface(start, tv_time))

        webView2.loadUrl("file:///android_asset/jd/index.html")
    }

    override fun onDestroy() {
        super.onDestroy()
//        webView2.clearCache(true)
    }
}