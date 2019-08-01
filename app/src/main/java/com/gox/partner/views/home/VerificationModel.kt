package com.gox.partner.views.home

import com.gox.base.base.BaseApplication
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey

class VerificationModel {

    var isService: Int = -1
    var isDocument: Int = -1
    var isBankDetail: Int = -1
    var providerStatus: String = ""
    var providerWalletBalance: Double = 0.0

    fun isNeedToShowPendingDialog(): Boolean {
        if (isService <= 0 || isDocument <= 0 || isBankDetail <= 0) return true
        else if (!providerStatus.equals("APPROVED", true)) return true
        else if (providerWalletBalance < 0) return true
        return false
    }

    fun getDialogType(): String {
        if (isService <= 0 || isDocument <= 0 || isBankDetail <= 0) return Constants.ProviderStatus.PENDING
        else if (!providerStatus.equals("APPROVED", true)) return Constants.ProviderStatus.WAITING
        else if (providerWalletBalance < BaseApplication.getCustomPreference!!.getLong(PreferencesKey.PROVIDER_NEGATIVE_BALANCE,0)) return Constants.ProviderStatus.LOW_BALANCE
        return ""
    }
}