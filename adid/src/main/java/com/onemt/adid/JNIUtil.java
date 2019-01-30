package com.onemt.adid;

import android.app.Activity;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/25 11:16
 * @see
 */
public class JNIUtil {

    static {
        System.loadLibrary("Utils");
    }

    public native String getAndroidId(Activity activity);
}
