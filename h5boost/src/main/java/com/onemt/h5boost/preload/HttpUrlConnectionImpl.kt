package com.onemt.h5boost.preload

import android.os.Handler
import android.os.Looper
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.GZIPInputStream

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/22 20:33
 * @see
 */
class HttpUrlConnectionImpl(private var session: Session): AbsConnection() {

    private val url = URL(session.url)
    private var conn: HttpURLConnection = url.openConnection() as HttpURLConnection
    private var ins: InputStream? = null
    private var ous = ByteArrayOutputStream()
    private val BUF_SIZE = 1024
    private var content = ""
    private val handler = Handler(Looper.getMainLooper())

    override fun connect(): String {
        try {
            conn.connect()
            ins = if ("gzip" == conn.contentEncoding) {
                BufferedInputStream(GZIPInputStream(conn.inputStream))
            } else {
                BufferedInputStream(conn.inputStream)
            }
            var buf = ByteArray(BUF_SIZE)
            var len = ins?.read(buf) ?: -1
            while (len != -1) {
                ous.write(buf, 0, len)
//                session.setInputStream(BufferedInputStream(ByteArrayInputStream(ous.toByteArray())))
//                handler.post { session.webView?.loadUrl(session.url) }
                len = ins?.read(buf) ?: -1
            }
            content = ous.toString("utf-8")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            ins?.close()
            ous?.close()
            conn.disconnect()
        }
        return content
    }
}