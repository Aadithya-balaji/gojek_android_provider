package com.gox.base.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("app:setAdapter")
fun <T : RecyclerView.ViewHolder> setAdapter(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<T>
) {
    recyclerView.setHasFixedSize(true)
    recyclerView.adapter = adapter
}