package com.appoets.xjek.ui.invitereferals

import com.appoets.basemodule.base.BaseViewModel

public class InviteReferalsViewModel : BaseViewModel<InviteReferalsNavigator>() {
    fun shareMyReferalCode() {
        navigator.goToInviteOption()
    }
}