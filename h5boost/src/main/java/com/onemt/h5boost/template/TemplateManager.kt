package com.onemt.h5boost.template

import android.content.Context
import android.widget.Toast
import org.jetbrains.anko.runOnUiThread
import org.json.JSONObject
import java.io.*
import java.net.URL
import java.util.concurrent.Executors

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/2 21:04
 * @see
 */
object TemplateManager {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
        Thread {
            val paths = context.assets.list("jd")
            val baseDir = File("${context.filesDir.absolutePath}${File.separator}jd")
            if (!baseDir.exists()) {
                baseDir.mkdirs()
            }
            if (paths?.size ?: 0 > 1) {
                for (path in paths) {
                    copy("jd${File.separator}$path", "${baseDir.absolutePath}${File.separator}$path")
                }
            } else {
                copyFile("jd", baseDir.absolutePath)
            }
            context.runOnUiThread { Toast.makeText(context, "init done", Toast.LENGTH_SHORT).show() }
        }.start()
    }

    private fun copy(srcPath: String, dstPath: String) {
        val paths = context.assets.list(srcPath)
        if (paths?.size ?: 0 > 0) {
            val file = File(dstPath)
            file.mkdirs()
            for (path in paths) {
                copy("$srcPath${File.separator}$path", "$dstPath${File.separator}$path")
            }
        } else {
            copyFile(srcPath, dstPath)
        }
    }

    private fun copyFile(srcPath: String, dstPath: String) {
        val file = File(dstPath)
        if (!file.exists()) {
            if (file.createNewFile()) {
                val ins = context.assets.open(srcPath)
                val fos = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var len = ins.read(buffer)
                while (len != -1) {
                    fos.write(buffer, 0, len)
                    len = ins.read(buffer)
                }
                ins.close()
                fos.close()
            }
        }
    }

    fun getData(callback: Callback) {
        Executors.newSingleThreadExecutor().execute {
            var url = URL("http://39.108.129.246:3000")
            var conn = url.openConnection()
            var ins = conn.getInputStream()
            var buffer = ByteArray(1024)
            var len = ins.read(buffer)
            var ous = ByteArrayOutputStream()
            var json = StringBuilder()
            while (len != -1) {
                ous.write(buffer, 0, len)
                json.append(String(buffer, 0, len))
                len = ins.read(buffer)
            }
            ins.close()
            ous.close()
            var data = JSONObject(json.toString())
            var result = Data()
            result.slide = data.getString("slide")
            result.navigation = data.getString("navigation")
            callback.onResponse(result)
        }

    }

    fun loadTemplate(templateCallback: TemplateCallback) {
        Executors.newSingleThreadExecutor().execute {
            val file = File("${context.filesDir}${File.separator}jd${File.separator}index.html")
            if (file.exists()) {
                var fis = BufferedReader(InputStreamReader(FileInputStream(file), "utf-8"))
                var line = fis.readLine()
                var sb = StringBuilder()
                while (line != null) {
                    sb.append(line)
                    sb.append("\n")
                    line = fis.readLine()
                }
                fis.close()
                templateCallback.onTemplateLoaded(sb.toString())
            }
        }
    }
}