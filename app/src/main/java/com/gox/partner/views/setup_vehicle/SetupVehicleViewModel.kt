package com.gox.partner.views.setup_vehicle

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.repository.ApiListener
import com.gox.partner.models.SetupRideResponseModel
import com.gox.partner.models.SetupShopResponseModel
import com.gox.partner.repository.AppRepository

class SetupVehicleViewModel : BaseViewModel<SetupVehicleNavigator>() {

    private val mRepository = AppRepository.instance()
    private val vehicleLiveData = MutableLiveData<Any>()
    private val transportServiceName: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.TRANSPORT_ID,Constants.ModuleTypes.TRANSPORT)
    private val orderServiceName: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.ORDER_ID, Constants.ModuleTypes.ORDER)

    private var serviceName: String = Constants.ModuleTypes.TRANSPORT
    private val adapter: SetupVehicleAdapter = SetupVehicleAdapter(this)

    fun getTransportId() = transportServiceName
    fun getOrderId() = orderServiceName

    fun setServiceId(serviceId: String) {
        this.serviceName = serviceId
    }

    fun getServiceName() = serviceName

    fun setAdapter() = adapter.notifyDataSetChanged()

    fun getAdapter() = adapter

    fun getItemCount(): Int {
        return if (vehicleLiveData.value == null)
            0
        else {
            when (serviceName) {
                transportServiceName -> (vehicleLiveData.value as SetupRideResponseModel).responseData.size
                orderServiceName -> (vehicleLiveData.value as SetupShopResponseModel).responseData.size
                else -> 0
            }
        }
    }

    fun getVehicleName(position: Int): String {
        return when (serviceName) {
            transportServiceName -> (vehicleLiveData.value as SetupRideResponseModel).responseData[position].rideName
            orderServiceName -> (vehicleLiveData.value as SetupShopResponseModel).responseData[position].name
            else -> return ""
        }
    }

    fun isVehicleAdded(position: Int): Boolean {
        return when (serviceName) {
            transportServiceName -> (vehicleLiveData.value as SetupRideResponseModel).responseData[position].providerService != null
            orderServiceName -> (vehicleLiveData.value as SetupShopResponseModel).responseData[position].providerService != null
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