package com.xjek.base.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("android:adapter")
fun <T : RecyclerView.ViewHolder> setAdapter(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<T>
) {
    recyclerView.adapter = adapter
}