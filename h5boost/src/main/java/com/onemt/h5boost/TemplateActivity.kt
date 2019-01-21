package com.onemt.h5boost

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.Looper
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
import com.onemt.h5boost.template.TemplateCallback
import com.onemt.h5boost.template.TemplateManager
import kotlinx.android.synthetic.main.normal_activity.*
import java.io.File

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 14:27
 * @see
 */
class TemplateActivity: BaseH5Activity() {

    private var data = MainActivity.data
    private val SLIDE_TAG = "<!--slider-->"
    private val NAV_TAG = "<!--navigation-->"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = "file:///${filesDir.absolutePath}${File.separator}jd${File.separator}"
        loadDefault(url)
        load.setOnClickListener {
            start = System.currentTimeMillis()
            jsInterface.updateStartTime(start)
            load()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.normal_activity
    }

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

        load()
    }

    private fun load() {
        TemplateManager.loadTemplate(object : TemplateCallback {
            override fun onTemplateLoaded(template: String) {
                if (template.isNotEmpty()) {
                    val result = template.replace(SLIDE_TAG, data?.slide ?: "")
                        .replace(NAV_TAG, data?.navigation ?: "")
                    Log.e("thread", Thread.currentThread().name)
                    Log.e("onTemplateLoaded", result)
                    runOnUiThread { webView2.loadDataWithBaseURL(url, result, "text/html", "utf-8", null) }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        webView2.clearCache(true)
        webView2.destroy()
    }
}