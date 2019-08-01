package com.gox.xuberservice.extracharge

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel

class XUberExtraChargeViwModel : BaseViewModel<XUberExtraChargeNavigator>() {
    var extraAmount = MutableLiveData("")
    var extraAmountNotes = MutableLiveData("")

    fun submitExtraCharge() = navigator.submit()
}