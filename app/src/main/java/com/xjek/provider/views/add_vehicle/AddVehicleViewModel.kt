package com.xjek.provider.views.add_vehicle

import android.view.View
import com.xjek.base.base.BaseViewModel

class AddVehicleViewModel : BaseViewModel<AddVehicleNavigator>() {

    fun onVehicleImageClick(view: View) {
        navigator.onVehicleImageClicked()
    }

    fun onRcBookClick(view: View) {
        navigator.onRcBookClicked()
    }

    fun onInsuranceClick(view: View) {
        navigator.onInsuranceClicked()
    }

    fun onVehicleSubmitClick(view: View) {
        navigator.onVehicleSubmitClicked()
    }
}