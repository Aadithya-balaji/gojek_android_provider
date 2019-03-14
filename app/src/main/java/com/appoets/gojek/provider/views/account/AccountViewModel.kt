package com.appoets.gojek.provider.views.account

import com.appoets.basemodule.base.BaseViewModel

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
