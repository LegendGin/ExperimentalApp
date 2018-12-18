package com.onemt.skin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/18 15:27
 * @see
 */
class PathView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var color: Int

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        paint.color = context.resources.getColor(R.color.colorAccent)
        paint.fontMetrics.top
        paint.fontMetrics.bottom
        paint.fontMetrics.ascent
        paint.fontMetrics.descent
        paint.fontMetrics.leading

        val a = context.obtainStyledAttributes(attrs, R.styleable.PathView)
        color = a.getColor(R.styleable.PathView_path_color, paint.color)
        a.recycle()
    }

    fun setPathColor(color: Int) {
        this.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(color)
        canvas.drawText("hello", 0.0f, height / 2.0f, paint)
    }
}