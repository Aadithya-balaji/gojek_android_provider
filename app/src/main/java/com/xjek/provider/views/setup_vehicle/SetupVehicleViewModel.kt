package com.xjek.provider.views.setup_vehicle

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.SetupRideResponseModel
import com.xjek.provider.models.SetupShopResponseModel
import com.xjek.provider.repository.AppRepository

class SetupVehicleViewModel : BaseViewModel<SetupVehicleNavigator>() {

    private val appRepository = AppRepository.instance()
    private val vehicleLiveData = MutableLiveData<Any>()
    private val transportId: Int = readPreferences(PreferencesKey.TRANSPORT_ID)
    private val orderId: Int = readPreferences(PreferencesKey.ORDER_ID)

    private var serviceId: Int = -1
    private val adapter: SetupVehicleAdapter = SetupVehicleAdapter(this)

    fun getTransportId() = transportId

    fun getOrderId() = orderId

    fun setServiceId(serviceId: Int) {
        this.serviceId = serviceId
    }

    fun getServiceId() = serviceId

    fun setAdapter() {
        adapter.notifyDataSetChanged()
    }

    fun getAdapter() = adapter

    fun getItemCount(): Int {
        return if (vehicleLiveData.value == null)
            0
        else {
            when (serviceId) {
                readPreferences<Int>(PreferencesKey.TRANSPORT_ID) -> (vehicleLiveData.value as SetupRideResponseModel).responseData.size
                readPreferences<Int>(PreferencesKey.ORDER_ID) -> (vehicleLiveData.value as SetupShopResponseModel).responseData.size
                else -> 0
            }
        }
    }

    fun getVehicleName(position: Int): String {
        return when (serviceId) {
            transportId -> (vehicleLiveData.value as SetupRideResponseModel).responseData[position].rideName
            orderId -> (vehicleLiveData.value as SetupShopResponseModel).responseData[position].name
            else -> return ""
        }
    }

    fun isVehicleAdded(position: Int): Boolean {
        return when (serviceId) {
            transportId -> (vehicleLiveData.value as SetupRideResponseModel).responseData[position].providerService != null
            orderId -> (vehicleLiveData.value as SetupShopResponseModel).responseData[position].providerService != null
            else -> false
        }
    }

    fun getRides() {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        getCompositeDisposable().add(appRepository.getRides(this, token))
    }

    fun getShops() {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        getCompositeDisposable().add(appRepository.getShops(this, token))
    }

    fun getVehicleDataObservable() = vehicleLiveData

    fun onItemClick(position: Int) {
        navigator.onMenuItemClicked(position)
    }
}