package com.onemt.h5boost

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.onemt.h5boost.preload.WebViewPool
import com.tencent.sonic.sdk.SonicEngine
import com.tencent.sonic.sdk.SonicSession
import com.tencent.sonic.sdk.SonicSessionConfig
import kotlinx.android.synthetic.main.browser_activity.*
import java.io.File

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 14:27
 * @see
 */
class BrowserActivity: AppCompatActivity() {

    private var sonicSession: SonicSession? = null
    private var url = MainActivity.url
    private var start = System.currentTimeMillis()
    private val webView1 = WebViewPool.obtain()
    private val webView2 = WebViewPool.obtain()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.browser_activity)
        fl_web.addView(webView1)
//        checkPermission()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
//        loadDefault(url)
        load(url)
        load.setOnClickListener {
            loadDefault(url)
        }
        sonic.setOnClickListener {
            load(url)
        }
    }

    private fun checkPermission() {
        if (PermissionChecker.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NETWORK_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE)) {
                Toast.makeText(
                    this,
                    "request ${Manifest.permission.ACCESS_NETWORK_STATE} permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_NETWORK_STATE), 0)
        } else {
            load(url)
        }
    }

    private fun loadDefault(url: String) {
        webView2.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("default", "${System.currentTimeMillis() - start}ms")
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    load(url)
                }
            }
        }
    }

    private fun load(url: String) {
        var sonicSessionClient = SonicSessionClientImpl()
        sonicSession = SonicEngine.getInstance().createSession(url, SonicSessionConfig.Builder().build())
        sonicSession?.bindClient(sonicSessionClient)
        webView1.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                sonicSession?.sessionClient?.pageFinish(url)
                Log.e("sonic", "${System.currentTimeMillis() - start}ms")
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                return shouldInterceptRequest(view, request?.url.toString())
            }

            override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                return sonicSession?.sessionClient?.requestResource(url) as WebResourceResponse?
            }
        }

        val webSettings = webView1.settings
        webSettings.javaScriptEnabled = true
        webView1.removeJavascriptInterface("searchBoxJavaBridge_")

        webSettings.allowContentAccess = true
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.savePassword = false
        webSettings.saveFormData = false
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true

        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(webView1)
            sonicSessionClient.clientReady()
        } else { // default mode
            webView1.loadUrl(url)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sonicSession?.destroy()
        sonicSession = null
        fl_web.removeView(webView2)
        WebViewPool.release(webView2)
    }
}