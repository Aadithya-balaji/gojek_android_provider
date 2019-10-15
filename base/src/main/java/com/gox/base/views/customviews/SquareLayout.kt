package com.gox.base.views.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class SquareLayout : LinearLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(width: Int, height: Int) {
        // note we are applying the width value as the height
        super.onMeasure(width, width)
    }

}