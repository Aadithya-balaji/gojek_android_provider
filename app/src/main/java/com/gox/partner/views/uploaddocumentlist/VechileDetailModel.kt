package com.gox.partner.views.uploaddocumentlist

import com.gox.base.base.BaseViewModel

class VechileDetailModel : BaseViewModel<VechileDetailNavigator>() {
    fun gotoVerificationProofPage() {
        navigator.gotoVerificationPage()
    }
}
