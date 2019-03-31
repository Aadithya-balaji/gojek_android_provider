package com.xjek.xjek.ui.profile

import com.xjek.base.base.BaseViewModel

public class ProfileViewModel : BaseViewModel<ProfileNavigator>() {
    fun moveToMyAccount() {
        navigator.goToMainActivity()
    }

}