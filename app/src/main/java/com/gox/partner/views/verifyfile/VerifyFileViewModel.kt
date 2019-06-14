package com.gox.partner.views.verifyfile

import com.gox.base.base.BaseViewModel

class VerifyFileViewModel : BaseViewModel<VerifyFileNavigator>() {
    fun showDashboardPage() {
        navigator.gotoDashBoardPage()
    }
}