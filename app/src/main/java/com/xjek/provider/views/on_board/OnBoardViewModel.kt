package com.xjek.provider.views.on_board

import android.view.View
import com.xjek.base.base.BaseViewModel

class OnBoardViewModel : BaseViewModel<OnBoardNavigator>() {

    fun onSignInClick(view: View) {
        navigator.onSignInClicked()
    }

    fun onSignUpClick(view: View) {
        navigator.onSignUpClicked()
    }
}
