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
import kotlinx.android.synthetic.main.normal_activity.*

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 14:27
 * @see
 */
class ParallelActivity: AppCompatActivity() {

    private var url = MainActivity.url
    private var start = System.currentTimeMillis()
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        session = H5Engine.createSession(url)
        H5Engine.preload(session)
        Log.e("onCreate", "start render")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.normal_activity)
        session.attach(webView2)
        loadDefault(url)
        load.setOnClickListener {
            start = System.currentTimeMillis()
            webView2.loadUrl(url)
        }
        Log.e("onCreate", "finish render:${System.currentTimeMillis() - start}")
    }

    private fun loadDefault(url: String) {
        webView2.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("onPageFinished", "${System.currentTimeMillis() - start}ms")
            }

            /*@TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                return shouldInterceptRequest(view, request?.url?.toString())
            }

            override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                Log.e("shouldInterceptRequest", "shouldInterceptRequest")
                return WebResourceResponse("text/html", "utf-8", session.getInputStream())
            }*/
        }

        WebviewHelper.setUp(webView2, JsInterface(start, tv_time))
    }

    override fun onDestroy() {
        super.onDestroy()
        webView2.clearCache(true)
    }
}