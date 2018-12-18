package com.onemt.skin

import android.os.Bundle

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/12 15:55
 * @see
 */
class MainActivity: BaseSkinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onPause() {
        super.onPause()
        SkinManager.getSize()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        SkinManager.getSize()
    }
}