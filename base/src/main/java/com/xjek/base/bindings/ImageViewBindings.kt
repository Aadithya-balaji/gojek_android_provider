package com.xjek.base.bindings

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

//@BindingAdapter("android:src")
//fun setImage(imageView: ImageView, imageUrl: String) {
//    Glide.with(imageView).load(imageUrl).into(imageView)
//}