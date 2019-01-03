package com.onemt.h5boost

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.webkit.CookieManager
import com.tencent.sonic.sdk.SonicRuntime
import com.tencent.sonic.sdk.SonicSessionClient
import java.io.File
import java.io.InputStream
import android.os.Build
import android.R.attr.data
import android.R.attr.mimeType
import android.webkit.WebResourceResponse
import android.text.TextUtils





/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 14:37
 * @see
 */
class SonicRuntimeImpl(context: Context): SonicRuntime(context) {

    override fun getUserAgent(): String {
        return "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Mobile Safari/537.36"
    }

    override fun getCurrentUserAccount(): String {
        return "sonic-demo-master"
    }

    override fun showToast(text: CharSequence?, duration: Int) {
    }

    override fun log(tag: String?, level: Int, message: String?) {
        Log.i(tag, message)
    }

    override fun isNetworkValid(): Boolean {
        val netManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = netManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    override fun postTaskToThread(task: Runnable?, delayMillis: Long) {
        val thread = Thread(task, "SonicThread")
        thread.start()
    }

    override fun isSonicUrl(url: String?): Boolean {
        return true
    }

    override fun setCookie(url: String?, cookies: MutableList<String>?): Boolean {
        if (!TextUtils.isEmpty(url) && cookies != null && cookies.size > 0) {
            val cookieManager = CookieManager.getInstance()
            for (cookie in cookies) {
                cookieManager.setCookie(url, cookie)
            }
            return true
        }
        return false
    }

    override fun getCookie(url: String?): String? {
        return CookieManager.getInstance().getCookie(url)
    }

    override fun createWebResourceResponse(
        mimeType: String?,
        encoding: String?,
        data: InputStream?,
        headers: MutableMap<String, String>?
    ): Any {
        val resourceResponse = WebResourceResponse(mimeType, encoding, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resourceResponse.responseHeaders = headers
        }
        return resourceResponse
    }

    override fun notifyError(client: SonicSessionClient?, url: String?, errorCode: Int) {
    }

}