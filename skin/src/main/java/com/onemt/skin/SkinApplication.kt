package com.onemt.skin

import android.app.Application

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/17 17:18
 * @see
 */
class SkinApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        ResourceLoader.init(this)
        ResourceLoaders.init(this)
    }
}