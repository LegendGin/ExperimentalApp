package com.onemt.skin

import android.content.Context
import android.os.Bundle
import android.util.ArrayMap
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import com.onemt.base.BaseActivity
import org.jetbrains.anko.info

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/12 16:37
 * @see
 */
open class BaseSkinActivity: BaseActivity() {

    private val skinAttrs = ArrayMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        SkinManager.register(this)
        super.onCreate(savedInstanceState)
    }
}