package com.onemt.h5boost

import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.onemt.h5boost.preload.H5Engine
import com.onemt.h5boost.preload.WebViewPool
import com.onemt.h5boost.template.Callback
import com.onemt.h5boost.template.Data
import com.onemt.h5boost.template.TemplateManager
import kotlinx.android.synthetic.main.main_activity.*
import java.io.File
import java.io.FileOutputStream

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 17:33
 * @see
 */
class MainActivity: AppCompatActivity() {
//    private var url = "http://mc.vip.qq.com/demo/indexv3"
    companion object {
//        var url = "http://eventtest.onemt.co/notice/notice/?gameid=100000801"
//        var url = "https://www.taobao.com"
        var url = "http://39.108.129.246:8080/jd/"
        var data: Data? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
//        val config = SonicSessionConfig.Builder().setSupportLocalServer(false).build()
//        val preloadSuccess = SonicEngine.getInstance().preCreateSession(url, config)
//        WebViewPool.obtain()?.loadUrl(url)
//        Log.e(localClassName, "preload:$preloadSuccess")
        et_url.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.toString()?.isNotEmpty() == true) {
                    url = s?.toString()
                } else {
                    url = "http://39.108.129.246:8080/jd/"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        global.setOnClickListener {
            startActivity(Intent(this, GlobalActivity::class.java))
        }
        preload.setOnClickListener {
            WebViewPool.init(this)
//            val session = H5Engine.createSession(url)
//            H5Engine.preload(session)
        }
        local.setOnClickListener {
            startActivity(Intent(this, LocalActivity::class.java))
        }
        normal.setOnClickListener {
            startActivity(Intent(this, NormalActivity::class.java))
        }
        normalWithDestroy.setOnClickListener {
            startActivity(Intent(this, NormalActivityWithDestroy::class.java))
        }
        parallel.setOnClickListener {
            startActivity(Intent(this, ParallelActivity::class.java))
        }
        parallel2.setOnClickListener {
            startActivity(Intent(this, Parallel2Activity::class.java))
        }
        template.setOnClickListener {
            startActivity(Intent(this, TemplateActivity::class.java))
        }
        init.setOnClickListener {
            TemplateManager.init(this)
            TemplateManager.getData(object : Callback {
                override fun onResponse(data: Data?) {
                    MainActivity.data = data
                    Log.e("data", "result:${data?.toString()}")
                }
            })
        }
        Toast.makeText(this, getAndroidId(), Toast.LENGTH_LONG).show()
    }

    private fun getAndroidId(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        System.exit(0)
        Process.killProcess(Process.myPid())
    }
}