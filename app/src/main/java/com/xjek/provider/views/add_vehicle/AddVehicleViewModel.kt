package com.xjek.provider.views.add_vehicle

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.createMultipartBody
import com.xjek.base.extensions.createRequestBody
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.AddVehicleDataModel
import com.xjek.provider.models.AddVehicleResponseModel
import com.xjek.provider.models.ProviderVehicleResponseModel
import com.xjek.provider.models.VehicleCategoryResponseModel
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository
import okhttp3.RequestBody
import java.io.File
import androidx.databinding.BindingAdapter



class AddVehicleViewModel : BaseViewModel<AddVehicleNavigator>() {

    private val appRepository = AppRepository.instance()
    private val transportId: Int = readPreferences(PreferencesKey.TRANSPORT_ID)
    private val orderId: Int = readPreferences(PreferencesKey.ORDER_ID)
    private val vehicleCategoryLiveData = MutableLiveData<VehicleCategoryResponseModel>()
    private val vehicleResponseLiveData = MutableLiveData<AddVehicleResponseModel>()

    private var serviceId: Int = -1
    private val addVehicleDataModel = AddVehicleDataModel()
    private val vehicleLiveData = MutableLiveData<AddVehicleDataModel>()
    private lateinit var vehicleUri: Uri
    private lateinit var rcBookUri: Uri
    private lateinit var insuranceUri: Uri

    fun getTransportId() = transportId

    fun getOrderId() = orderId

    fun setServiceId(serviceId: Int) {
        this.serviceId = serviceId
    }

    fun getServiceId() = serviceId

    fun setVehicleLiveData(providerVehicle: ProviderVehicleResponseModel) {
        addVehicleDataModel.vehicleImage = providerVehicle.vehicleImage
        addVehicleDataModel.vehicleModel = providerVehicle.vehicleModel
        addVehicleDataModel.vehicleYear = providerVehicle.vehicleYear.toString()
        addVehicleDataModel.vehicleColor = providerVehicle.vehicleColor
        addVehicleDataModel.vehicleNumber = providerVehicle.vehicleNo
        addVehicleDataModel.vehicleMake = providerVehicle.vehicleMake
        addVehicleDataModel.vehicleRcBook = providerVehicle.picture
        addVehicleDataModel.vehicleInsurance = providerVehicle.picture1
        vehicleLiveData.value = addVehicleDataModel
    }

    fun setVehicleUri(vehicleUri: Uri) {
        this.vehicleUri = vehicleUri
    }

    fun setRcBookUri(rcBookUri: Uri) {
        this.rcBookUri = rcBookUri
    }

    fun setInsuranceUri(insuranceUri: Uri) {
        this.insuranceUri = insuranceUri
    }

    fun isFieldMandatory(): Boolean {
        return when (serviceId) {
            transportId -> {
                true
            }
            orderId -> {
                false
            }
            else -> false
        }
    }

    fun isVehicleDataValid(): Boolean {
        if (addVehicleDataModel.vehicleModel.isNullOrBlank()) {
            return false
        }
        return false
    }

    fun getVehicleCategories() {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        getCompositeDisposable().add(appRepository.getVehicleCategories(this, token))
    }

    fun postVehicle() {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        val params = HashMap<String, RequestBody>()
        params[WebApiConstants.AddService.VEHICLE_ID] = createRequestBody("")
        params[WebApiConstants.AddService.VEHICLE_MODEL] =
                createRequestBody(getVehicleData()!!.vehicleModel.toString())
        params[WebApiConstants.AddService.VEHICLE_YEAR] =
                createRequestBody(getVehicleData()!!.vehicleYear.toString())
        params[WebApiConstants.AddService.VEHICLE_COLOR] =
                createRequestBody(getVehicleData()!!.vehicleColor.toString())
        params[WebApiConstants.AddService.VEHICLE_NO] = createRequestBody("")
        params[WebApiConstants.AddService.VEHICLE_MAKE] = createRequestBody("")
        val rcBookMultipart =
                createMultipartBody(WebApiConstants.AddService.PICTURE, "image/*",
                        File(rcBookUri.toString()))
        val insuranceMultipart =
                createMultipartBody(WebApiConstants.AddService.PICTURE, "image/*",
                        File(insuranceUri.toString()))
        getCompositeDisposable().add(appRepository.postVehicle(this, token, params,
                rcBookMultipart, insuranceMultipart))
    }

    fun getVehicleData() = vehicleLiveData.value

    fun getVehicleCategoryObservable() = vehicleCategoryLiveData

    fun getVehicleResponseObservable() = vehicleResponseLiveData

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