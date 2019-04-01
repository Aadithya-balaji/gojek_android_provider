package com.xjek.base.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

open class BaseViewModel<N> : ViewModel() {

    private var compositeDisposable = CompositeDisposable()
    private lateinit var mNavigator: WeakReference<N>

    var navigator: N
        get() = mNavigator.get()!!
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    fun getCompositeDisposable() = compositeDisposable

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
