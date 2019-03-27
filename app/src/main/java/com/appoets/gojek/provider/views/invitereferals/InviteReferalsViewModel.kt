package com.appoets.xjek.ui.invitereferals

import com.appoets.base.base.BaseViewModel

public class InviteReferalsViewModel : BaseViewModel<InviteReferalsNavigator>() {
    fun shareMyReferalCode() {
        navigator.goToInviteOption()
    }
}