package com.onemt.skin

import android.content.res.Resources

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/17 17:28
 * @see
 */
class SkinAttr(var attrName: String,
                    var resName: String,
               var resType: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SkinAttr

        if (attrName != other.attrName) return false
        if (resName != other.resName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attrName.hashCode()
        result = 31 * result + resName.hashCode()
        return result
    }

    fun getResId(res: Resources, packageName: String): Int {
        return try {
            res.getIdentifier(resName, resType,packageName)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            0
        }
    }
}