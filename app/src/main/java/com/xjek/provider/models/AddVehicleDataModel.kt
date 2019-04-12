package com.xjek.provider.models

import androidx.lifecycle.MutableLiveData

data class AddVehicleDataModel(
//        var vehicleImage: MutableLiveData<String?>,
//        var categoryId: MutableLiveData<String?>,
        var vehicleModel: MutableLiveData<String?>,
        var vehicleYear: MutableLiveData<String?>,
        var vehicleColor: MutableLiveData<String?>,
        var vehiclePlate: MutableLiveData<String?>,
        var vehicleMake: MutableLiveData<String?>,
        var vehicleRcBook: MutableLiveData<String?>,
        var vehicleInsurance: MutableLiveData<String?>
)