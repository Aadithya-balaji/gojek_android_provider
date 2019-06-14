package com.gox.partner.views.on_board

import com.gox.base.base.BaseViewModel
import io.reactivex.disposables.CompositeDisposable

class OnBoardViewModel : BaseViewModel<OnBoardNavigator>() {

    private val mCompositeDisposable = CompositeDisposable()

    fun openSignIn() = navigator.goToSignIn()

    fun openSignUp() = navigator.goToSignUp()

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}