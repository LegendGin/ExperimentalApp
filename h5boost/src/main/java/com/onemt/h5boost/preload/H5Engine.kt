package com.onemt.h5boost.preload

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.tencent.sonic.sdk.SonicUtils
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/22 20:28
 * @see
 */
object H5Engine {

    private val executor = ThreadPoolExecutor(5, 5,
        60, TimeUnit.SECONDS,
        LinkedBlockingQueue<Runnable>(10))

    private var connFactory: ConnectionFactory = DefaultConnFactory()

    private var mainHandler = Handler(Looper.getMainLooper())

    private var cacheDir = ""

    fun init(context: Context) {
        cacheDir = context.cacheDir.absolutePath
    }

    fun createSession(url: String) = Session(url)

    fun preload(session: Session) {
        executor.execute(LoadUntilDataReadyTask(session))
    }

    fun preload2(session: Session) {
        connFactory = DefaultConnFactory2()
        executor.execute(SimultaneousTask(session))
    }

    /**
     * 等待数据读取完毕才交给webview进行渲染
     */
    private class LoadUntilDataReadyTask(var session: Session): Runnable {
        override fun run() {
            val conn = connFactory.getConn(session)
            val response = conn.connect()
            if (response.isNotEmpty()) {
                val file = File("$cacheDir${File.separator}${SonicUtils.getMD5(session.url)}")
                if (file.createNewFile()) {
                    val out = FileOutputStream(file)
                    out.write(response.toByteArray())
                    out.close()
                    Log.e("create file", file.name)
                } else {
                    Log.e("create file", "failed")
                }
                mainHandler.post {
                    Log.e("loadData", "loadDataWithBaseURL")
                    session.webView?.loadDataWithBaseURL(session.url,
                        response,
                        "text/html",
                        "utf-8",
                        session.url)
                }
            }
        }
    }

    private class SimultaneousTask(var session: Session): Runnable, Handler.Callback {
        override fun run() {
            val conn = connFactory.getConn(session)
            val response = conn.connect()
            /*if (response.isNotEmpty()) {
                mainHandler.post {
                    Log.e("loadData", "loadDataWithBaseURL")
                    session.webView?.loadDataWithBaseURL(session.url,
                        response,
                        "text/html",
                        "utf-8",
                        session.url)
                }
            }*/
        }

        override fun handleMessage(msg: Message?): Boolean {
            return true
        }
    }
}