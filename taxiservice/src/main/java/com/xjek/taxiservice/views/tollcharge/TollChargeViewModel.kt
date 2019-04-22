package com.xjek.taxiservice.views.tollcharge

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel

class  TollChargeViewModel :BaseViewModel<TollChargeNavigator>(){
    var tollChargeLiveData=MutableLiveData<String>()

    fun submit(){
        navigator.addTollCharge()
    }
}