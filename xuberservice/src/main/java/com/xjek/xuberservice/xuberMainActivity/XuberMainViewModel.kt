package com.xjek.xuberservice.xuberMainActivity

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel

class XuberMainViewModel : BaseViewModel<XuberMainNavigator>() {

    var driverStatus: ObservableField<String> = ObservableField("Driver accepted your request")
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()

    fun pickLocation() {
        navigator.goToLocationPick()
    }

    fun goBack() {
        navigator.goBack()
    }

    fun showCurrentLocation() {
        navigator.showCurrentLocation()
    }

    fun moveStatusFlow() {
        navigator.moveStatusFlow()
    }

    fun setDriverStatus(status: String) {
        driverStatus.set(status)
        driverStatus.notifyChange()
    }
}