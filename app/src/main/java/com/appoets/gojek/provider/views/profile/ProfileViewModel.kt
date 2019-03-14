package com.appoets.xjek.ui.profile

import com.appoets.basemodule.base.BaseViewModel

public class ProfileViewModel : BaseViewModel<ProfileNavigator>() {
    fun moveToMyAccount() {
        navigator.goToMainActivity()
    }

}