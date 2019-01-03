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
import android.widget.TextView
import android.widget.Toast
import com.onemt.h5boost.preload.HttpClient
import kotlinx.android.synthetic.main.normal_activity.*

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 14:27
 * @see
 */
class NormalActivity: BaseH5Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadDefault(url)
        load.setOnClickListener {
            start = System.currentTimeMillis()
            jsInterface.updateStartTime(start)
            webView2.loadUrl(url)
        }
    }

    override fun getLayoutId() = R.layout.normal_activity

    override fun getTextView(): TextView {
        return tv_time
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
                    runOnUiThread {
                        Toast.makeText(application, "send request after ${System.currentTimeMillis() - start}ms", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("shouldInterceptRequest", "send request after ${System.currentTimeMillis() - start}ms")
                    startSendRequest = true
                }
                return super.shouldInterceptRequest(view, url)
            }
        }
        WebviewHelper.setUp(webView2, jsInterface)
        webView2.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        webView2.clearCache(true)
    }
}