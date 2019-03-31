package com.xjek.base.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <reified VM : ViewModel> FragmentActivity.provideViewModel(
        crossinline factory: () -> VM
): VM {
    return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = factory() as T
    }).get(VM::class.java)
}