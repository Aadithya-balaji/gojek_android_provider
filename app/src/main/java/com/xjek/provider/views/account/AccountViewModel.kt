package com.xjek.provider.views.account

import com.xjek.base.base.BaseViewModel

class AccountViewModel : BaseViewModel<AccountNavigator>() {
    fun openProfilePage() {
        navigator.gotoProfilePage()
    }

    fun openInvitePage() {
        navigator.gotoInvitPage()
    }

    fun openPaymentPage() {
        navigator.gotoPaymentPage()
    }


    fun openTranscationPage() {
        navigator.gotoTransacationPage()
    }

    fun openPrivacyPage() {
        navigator.gotoPrivacyPage()
    }

    fun openSupportPage() {
        navigator.gotoSupportPage()
    }
}
