package com.gox.base.bindings

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("android:src")
fun setImage(imageView: ImageView, drawable: Drawable) {
    imageView.setImageDrawable(drawable)
}

@BindingAdapter("android:src")
fun setImage(imageView: ImageView, bitmap: Bitmap) {
    imageView.setImageBitmap(bitmap)
}

@BindingAdapter("android:src")
fun setImage(imageView: ImageView, resId: Int) {
    imageView.setImageResource(resId)
}

@BindingAdapter("app:setImageResource")
fun setImage(imageView: ImageView, imageUrl: String?) {
    if (imageUrl.isNullOrBlank())
        Glide.with(imageView).load(imageUrl).into(imageView)
}