package com.xjek.provider.views.home

class VerificationModel {

    var isService: Int = -1
    var isDocument: Int = -1
    var isBankDetail: Int = -1
    var providerStatus: String = ""
    var providerWalletBalance: Double = 0.0

    fun isNeedToShowPendingDialog(): Boolean {
        if (isService <= 0 || isDocument <= 0 || isBankDetail <= 0) {
            return true
        } else if (!providerStatus.equals("APPROVED", true)) {
            return true
        } else if (providerWalletBalance < 0) {
            return true
        }
        return false
    }

    fun getDialogType():Int{
        if (isService <= 0 || isDocument <= 0 || isBankDetail <= 0) {
            return 1
        } else if (!providerStatus.equals("APPROVED", true)) {
            return 2
        } else if (providerWalletBalance < 0) {
            return 3
        }
        return 0
    }

}