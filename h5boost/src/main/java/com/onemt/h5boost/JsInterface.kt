package com.onemt.h5boost

import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.TextView

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/22 19:23
 * @see
 */
class JsInterface(var startTime: Long, var tv_time: TextView) {

    fun updateStartTime(startTime: Long) {
        this.startTime = startTime
    }

    @JavascriptInterface
    fun ready() {
        val cost = "cost:${System.currentTimeMillis() - startTime}ms"
        Log.e("ready", cost)
        tv_time.post {
            tv_time.text = cost
        }
    }

    @JavascriptInterface
    fun mounted() {
        val cost = "cost:${System.currentTimeMillis() - startTime}ms"
        Log.e("mounted", cost)
        /*tv_time.post {
            tv_time.text = cost
        }*/
    }

}