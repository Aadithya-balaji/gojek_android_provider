package com.xjek.xjek.ui.invitereferals

import com.xjek.base.base.BaseViewModel

public class InviteReferalsViewModel : BaseViewModel<InviteReferalsNavigator>() {
    fun shareMyReferalCode() {
        navigator.goToInviteOption()
    }
}