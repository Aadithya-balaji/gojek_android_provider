package com.xjek.provider.views.earnings

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout
import com.xjek.provider.views.earnings.EarningsPagerAdapter.Companion.BIG_SCALE

class EarningsItemLayout : LinearLayout {

    private var mScale = BIG_SCALE

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    fun setScaleBoth(scale: Float) {
        this.mScale = scale
        this.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.scale(mScale, mScale, (this.width / 2).toFloat(), (this.height / 2).toFloat())
        super.onDraw(canvas)
    }
}