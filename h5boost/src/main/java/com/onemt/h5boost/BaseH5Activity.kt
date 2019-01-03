package com.onemt.h5boost

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/28 14:49
 * @see
 */
abstract class BaseH5Activity: AppCompatActivity() {

    var url = MainActivity.url
    var start = System.currentTimeMillis()
    var startSendRequest = false
    lateinit var jsInterface: JsInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        if (!::jsInterface.isInitialized) {
            jsInterface = JsInterface(start, getTextView())
        }
    }

    abstract fun getLayoutId(): Int
    abstract fun getTextView(): TextView
}