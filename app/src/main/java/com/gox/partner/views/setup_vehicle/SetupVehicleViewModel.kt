package com.gox.partner.views.setup_vehicle

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.repository.ApiListener
import com.gox.partner.models.SetupDeliveryResponseModel
import com.gox.partner.models.SetupRideResponseModel
import com.gox.partner.models.SetupShopResponseModel
import com.gox.partner.repository.AppRepository

class SetupVehicleViewModel : BaseViewModel<SetupVehicleNavigator>() {

    private val mRepository = AppRepository.instance()
    private var vehicleLiveData = MutableLiveData<Any>()
    private val transportServiceName: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.TRANSPORT_ID,Constants.ModuleTypes.TRANSPORT) ?: ""
    private val orderServiceName: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.ORDER_ID, Constants.ModuleTypes.ORDER) ?: ""
    private val deliveryServiceName: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.DELIVERY_ID, Constants.ModuleTypes.DELIVERY) ?: ""

    private var serviceName: String = Constants.ModuleTypes.TRANSPORT
    private val adapter: SetupVehicleAdapter = SetupVehicleAdapter(this)

    fun getTransportId() = transportServiceName
    fun getOrderId() = orderServiceName
    fun getDeliveryId() = deliveryServiceName

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
                deliveryServiceName -> (vehicleLiveData.value as SetupDeliveryResponseModel).responseData.size
                else -> 0
            }
        }
    }

    fun getVehicleName(position: Int): String {
        return when (serviceName) {
            transportServiceName -> (vehicleLiveData.value as SetupRideResponseModel).responseData[position].rideName
            orderServiceName -> (vehicleLiveData.value as SetupShopResponseModel).responseData[position].name
            deliveryServiceName -> (vehicleLiveData.value as SetupDeliveryResponseModel).responseData[position].deliveryName
            else -> return ""
        }
    }

    fun isVehicleAdded(position: Int): Boolean {
        return when (serviceName) {
            transportServiceName ->{
                return if((vehicleLiveData.value as SetupRideResponseModel).responseData[position].providerService?.status == "ACTIVE"){
                    true
                }else (vehicleLiveData.value as SetupRideResponseModel).responseData[position].providerService?.status == "ASSESSING"
            }
            orderServiceName -> {
                return if((vehicleLiveData.value as SetupShopResponseModel).responseData[position].providerService?.status == "ACTIVE"){
                    true
                }else (vehicleLiveData.value as SetupShopResponseModel).responseData[position].providerService?.status == "ASSESSING"
            }
            deliveryServiceName -> {
                return if((vehicleLiveData.value as SetupDeliveryResponseModel).responseData[position].providerService?.status == "ACTIVE"){
                    true
                }else (vehicleLiveData.value as SetupDeliveryResponseModel).responseData[position].providerService?.status == "ASSESSING"
            }
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

    fun updateService(status:Boolean,id:Int,updateService:HashMap<String,String>,postion:Int) {
        getCompositeDisposable().add(mRepository.updateService(object : ApiListener {
            override fun success(successData: Any) {
                navigator.showSuccess(status,postion)
            }
            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        },id,updateService))
    }

    fun getDelivery() {
        getCompositeDisposable().add(mRepository.getDelivery(object : ApiListener {
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
    fun onItemSwitchClick(position: Int,status:Boolean) = navigator.switchOnCliked(position,status)


}