package com.onemt.skin

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import java.io.File

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/17 17:13
 * @see
 */
@SuppressLint("StaticFieldLeak")
object ResourceLoader {

    private var mResources: Resources? = null

    private var mContext: Context? = null

    fun init(context: Context) {
        mContext = context
    }

    fun get(file: File): Resources? {
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
        addAssetPath.invoke(assetManager, file.absolutePath)
        val superRes = mContext?.resources
        mResources = Resources(assetManager,superRes?.displayMetrics, superRes?.configuration)
        return mResources
    }
}