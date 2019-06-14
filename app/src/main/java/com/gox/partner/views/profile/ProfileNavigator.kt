package com.gox.partner.views.profile

import androidx.databinding.ObservableField

interface ProfileNavigator {

    fun profileUpdateValidation(email: String,
                                phoneNumber: String,
                                firstName: String,
                                country: String,
                                city: String
    ): Boolean

    fun goToCityListActivity(countryId: ObservableField<String>)
    fun goToChangePasswordActivity()
}