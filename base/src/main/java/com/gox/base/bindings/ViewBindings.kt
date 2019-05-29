package com.gox.base.bindings

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setVisibility")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
