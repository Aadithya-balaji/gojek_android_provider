package com.appoets.gojek.provider.views.uploaddocumentlist

import com.appoets.basemodule.base.BaseViewModel

class VechileDetailModel:BaseViewModel<VechileDetailNavigator>(){
    fun  gotoVerificationProofPage(){
        navigator.gotoVerificationPage()
    }
}
