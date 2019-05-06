package com.xjek.provider.views.uploaddocumentlist

import com.xjek.base.base.BaseViewModel

class VechileDetailModel : BaseViewModel<VechileDetailNavigator>() {
    fun gotoVerificationProofPage() {
        navigator.gotoVerificationPage()
    }
}
