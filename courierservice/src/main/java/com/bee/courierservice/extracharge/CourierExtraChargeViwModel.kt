package com.bee.courierservice.extracharge

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel

class CourierExtraChargeViwModel : BaseViewModel<CourierExtraChargeNavigator>() {
    var extraAmount = MutableLiveData("")
    var extraAmountNotes = MutableLiveData("")

    fun submitExtraCharge() = navigator.submit()
}