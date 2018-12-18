package com.onemt.skin

import android.content.Context
import android.content.res.Resources
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.info
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/14 16:55
 * @see
 */
object SkinManager : AnkoLogger{

    private val viewMap = WeakHashMap<View, MutableSet<SkinAttr>>()
    private var isDefaultTheme = true
    private var externalRes: Resources? = null

    init {
        val file = File("${Environment.getExternalStorageDirectory().absolutePath}${File.separator}skin${File.separator}skin.apk")
        if (file.exists()) {
            externalRes = ResourceLoader.get(file)
        } else {
            val ins = ResourceLoader.getContext()?.assets?.open("skin.apk")
            if (ins != null) {
//            val destDir = File("${context.cacheDir.absolutePath}${File.separator}skin")
                val destDir = File("${Environment.getExternalStorageDirectory().absolutePath}${File.separator}skin")
                if (!destDir.exists()) {
                    destDir.mkdirs()
                }
                val file = File("${Environment.getExternalStorageDirectory().absolutePath}${File.separator}skin.apk")
//            val file = File("${destDir.absolutePath}${File.separator}skin.apk")
                if (!file.exists()) {
                    file.createNewFile()
                }
                val out = FileOutputStream(file)
                val byteArr = ByteArray(1024)
                try {
                    var len = ins.read(byteArr)
                    while (len > 0) {
                        out.write(byteArr, 0, len)
                        len = ins.read(byteArr)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    ins.close()
                    out.close()
                    externalRes = ResourceLoader.get(file)
                }
            }
        }
    }

    fun getSize() = viewMap.size

    fun change(context: Context) {
        if (isDefaultTheme) {
            externalRes?.let {
                setTheme(it, "com.onemt.skinred")
            }
            isDefaultTheme = false
        } else {
            val res = context.resources
            setTheme(res, "com.onemt.skin")
            isDefaultTheme = true
        }
    }

    private fun setTheme(res: Resources, packageName: String) {
        viewMap.forEach { it ->
            val view = it.key
            val value = it.value
            value.forEach { skinAttr ->
                when (skinAttr.attrName) {
                    SkinConfig.TEXT_COLOR -> {
                        res?.let {
                            if (view is TextView) {
                                view.setTextColor(it.getColorStateList(skinAttr.getResId(it, packageName)))
                            }
                        }
                    }
                    SkinConfig.BACKGROUND -> {
                        res?.let {
                            view.backgroundColor = it.getColor(skinAttr.getResId(it, packageName))
                        }
                    }
                    SkinConfig.PATH_COLOR -> {
                        res?.let {
                            if (view is PathView) {
                                view.setPathColor(it.getColor(skinAttr.getResId(it, packageName)))
                            }
                        }
                    }
                }
            }
        }
    }

    fun register(context: Context) {
        LayoutInflater.from(context).factory2 = object : LayoutInflater.Factory2 {
            override fun onCreateView(parent: View?, name: String?, context: Context, attrs: AttributeSet): View? {
                val res = context.resources
                var view: View? = null
                if (context is AppCompatActivity) {
                    context.delegate.createView(parent, name, context, attrs)
                }
                if (view == null) {
                    view = createView(context, name, attrs)
                }
                view?.let {
                    viewMap[it] = mutableSetOf()
                }
                val support = attrs.getAttributeBooleanValue("http://schemas.android.com/android/skin", "support", true)
                if (support) {
                    for (i in 0 until attrs.attributeCount) {
                        val attrName = attrs.getAttributeName(i)
                        val attr = attrs.getAttributeResourceValue(i, -1)
                        var skinAttr: SkinAttr? = null
                        if (attr != -1) {
                            view?.let {
                                skinAttr = SkinAttr(attrName, res.getResourceEntryName(attr), res.getResourceTypeName(attr))
                                viewMap[it]?.add(skinAttr!!)
                            }
                        }
                        val name = if (attr != -1) {
                            when (attrName) {
                                SkinConfig.TEXT_COLOR -> {
                                    if (!isDefaultTheme) {
                                        if (view is TextView) {
                                            externalRes?.let {
                                                view.setTextColor(skinAttr?.getResId(it, "com.onemt.skinred")?.let { it1 ->
                                                    it.getColorStateList(
                                                        it1
                                                    )
                                                })
                                            }
                                        }
                                    }
                                    res?.getResourceEntryName(attr)
                                }
                                SkinConfig.BACKGROUND -> {
                                    if (!isDefaultTheme) {
                                        externalRes?.let {
                                            view?.backgroundColor = it.getColor(skinAttr!!.getResId(it, "com.onemt.skinred"))
                                        }
                                    }
                                    res?.getResourceEntryName(attr)
                                }
                                SkinConfig.PATH_COLOR -> {
                                    if (!isDefaultTheme) {
                                        externalRes?.let {
                                            if (view is PathView) {
                                                view.setPathColor(it.getColor(skinAttr!!.getResId(it, "com.onemt.skinred")))
                                            }
                                        }
                                    }
                                    res?.getResourceEntryName(attr)
                                }
                                else -> ""
                            }
                        } else ""
                        info { "$view : $attrName: $attr $name" }
                    }
                }
                return view
            }

            override fun onCreateView(name: String?, context: Context, attrs: AttributeSet): View? {
                return this.onCreateView(null, name, context, attrs)
            }

        }
    }

    private fun createView(context: Context, name: String?, attrs: AttributeSet): View? {
        // 对context是ContextThemeWrapper的情况进行try-catch
        try {
            val inflater = LayoutInflater.from(context)
            return if (name?.contains(".") == true) {
                inflater.createView(name, null, attrs)
            } else {
                var view: View? = null
                if ("View" == name || "ViewStub" == name || "ViewGroup" == name) {
                    view = inflater.createView(name, "android.view.", attrs)
                }
                if (view == null) {
                    view = inflater.createView(name, "android.widget.", attrs)
                }
                if (view == null) {
                    view = inflater.createView(name, "android.webkit.", attrs)
                }
                if (view == null) {
                    view = inflater.createView(name, "android.support.v7.widget.", attrs)
                }
                view
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}