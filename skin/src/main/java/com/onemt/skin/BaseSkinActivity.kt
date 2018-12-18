package com.onemt.skin

import android.os.Bundle
import com.onemt.base.BaseActivity

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/12 16:37
 * @see
 */
open class BaseSkinActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        SkinManager.register(this)
        super.onCreate(savedInstanceState)
    }
}