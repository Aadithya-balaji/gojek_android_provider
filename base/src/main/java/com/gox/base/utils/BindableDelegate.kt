package com.gox.base.utils

import androidx.databinding.BaseObservable
import kotlin.reflect.KProperty

class BindableDelegate<in R : BaseObservable, T : Any>(
        private var value: T,
        private val bindingId: Int
) {
    operator fun getValue(receiver: R, property: KProperty<*>): T = value

    operator fun setValue(receiver: R, property: KProperty<*>, value: T) {
        this.value = value
        receiver.notifyPropertyChanged(bindingId)
    }
}

fun <R : BaseObservable, T : Any> bindable(value: T, bindingId: Int): BindableDelegate<R, T> =
        BindableDelegate(value, bindingId)