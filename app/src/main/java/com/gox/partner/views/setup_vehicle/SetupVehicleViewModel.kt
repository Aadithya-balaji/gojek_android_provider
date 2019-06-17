package com.gox.partner.views.setup_vehicle

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.repository.ApiListener
import com.gox.partner.models.SetupRideResponseModel
import com.gox.partner.models.SetupShopResponseModel
import com.gox.partner.repository.AppRepository

class SetupVehicleViewModel : BaseViewModel<SetupVehicleNavigator>() {

    private val mRepository = AppRepository.instance()
    private val vehicleLiveData = MutableLiveData<Any>()
    private val transportId: Int = BaseApplication.getCustomPreference!!.getInt(PreferencesKey.TRANSPORT_ID, 1)
    private val orderId: Int = BaseApplication.getCustomPreference!!.getInt(PreferencesKey.ORDER_ID, 2)

    private var serviceId: Int = -1
    private val adapter: SetupVehicleAdapter = SetupVehicleAdapter(this)

    fun getTransportId() = transportId
    fun getOrderId() = orderId

    fun setServiceId(serviceId: Int) {
        this.serviceId = serviceId
    }

    fun getServiceId() = serviceId

    fun setAdapter() = adapter.notifyDataSetChanged()

    fun getAdapter() = adapter

    fun getItemCount(): Int {
        return if (vehicleLiveData.value == null)
            0
        else {
            when (serviceId) {
                transportId -> (vehicleLiveData.value as SetupRideResponseModel).responseData.size
                orderId -> (vehicleLiveData.value as SetupShopResponseModel).responseData.size
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
        getCompositeDisposable().add(mRepository.getRides(object : ApiListener {
            override fun success(successData: Any) {
                getVehicleDataObservable().value = successData
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        }))
    }

    fun getShops() {
        getCompositeDisposable().add(mRepository.getShops(object : ApiListener {
            override fun success(successData: Any) {
                getVehicleDataObservable().value = successData
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        }))
    }

    fun getVehicleDataObservable() = vehicleLiveData

    fun onItemClick(position: Int) = navigator.onMenuItemClicked(position)
}