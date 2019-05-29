package com.gox.partner.views.on_board

import android.view.View
import com.gox.base.base.BaseViewModel

class OnBoardViewModel : BaseViewModel<OnBoardNavigator>() {

    fun onSignInClick(view: View) {
        navigator.onSignInClicked()
    }

    fun onSignUpClick(view: View) {
        navigator.onSignUpClicked()
    }
}
