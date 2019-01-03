package com.onemt.h5boost.preload

import android.webkit.WebView
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/22 21:34
 * @see
 */
class Session(var url: String) {

    var webView: WebView? = null
    private var inputStream: InputStream? = null
    private var sb = StringBuffer()

    fun attach(webView: WebView) {
        this.webView = webView
    }

    fun setInputStream(inputStream: InputStream) {
        this.inputStream = inputStream
    }

    fun append(byteArray: ByteArray) {
        sb.append(String(byteArray))
    }

    fun getInputStream(): InputStream? {
        val content = sb.toString()
        sb = StringBuffer()
        return BufferedInputStream(ByteArrayInputStream(content.toByteArray()))
    }
}