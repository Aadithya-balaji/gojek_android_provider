package com.xjek.xuberservice.extracharge

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel

class XuperExtraChargeViwModel : BaseViewModel<XuperExtraChargeNavigator>() {
    var extraAmount=MutableLiveData<String>()
    var extraAmountNotes=MutableLiveData<String>()
    fun submitExtraCharge() {
        navigator.submit()
    }
}