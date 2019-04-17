package com.xjek.xjek.ui.profile

import androidx.databinding.ObservableField

interface ProfileNavigator {

    fun profileUpdateValidation(email: String,
                                phonenumber: String,
                                firstname: String,
                                country: String,
                                city: String
    ): Boolean

    fun goToCityListActivity(countryId: ObservableField<String>)
    fun goToChangePasswordActivity()
}