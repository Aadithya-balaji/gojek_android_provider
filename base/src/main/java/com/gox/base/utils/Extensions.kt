package com.gox.base.utils

import androidx.databinding.Observable

inline fun <reified T : Observable> T.observe(crossinline observer: (T) -> Unit): Observable.OnPropertyChangedCallback =
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                observer(sender as T)
            }
        }.also { addOnPropertyChangedCallback(it) }