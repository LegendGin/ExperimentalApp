package com.onemt.h5boost

import android.app.Application
import com.onemt.h5boost.preload.H5Engine
import com.onemt.h5boost.preload.WebViewPool
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine



/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/20 14:36
 * @see
 */
class H5Application: Application() {

    override fun onCreate() {
        super.onCreate()
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(SonicRuntimeImpl(this), SonicConfig.Builder().build())
        }
        H5Engine.init(this)
    }
}