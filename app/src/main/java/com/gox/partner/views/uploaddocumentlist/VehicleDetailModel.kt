package com.gox.partner.views.uploaddocumentlist

import com.gox.base.base.BaseViewModel

class VehicleDetailModel : BaseViewModel<VehicleDetailNavigator>() {
    fun gotoVerificationProofPage() {
        navigator.gotoVerificationPage()
    }
}