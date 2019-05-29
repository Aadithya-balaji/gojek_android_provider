package com.gox.partner.views.add_vehicle

import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.createMultipartBody
import com.gox.base.extensions.createRequestBody
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.AddVehicleDataModel
import com.gox.partner.models.AddVehicleResponseModel
import com.gox.partner.models.ProviderVehicleResponseModel
import com.gox.partner.models.VehicleCategoryResponseModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AddVehicleViewModel : BaseViewModel<AddVehicleNavigator>() {

    private val appRepository = AppRepository.instance()
    private val transportId: Int = BaseApplication.getCustomPreference!!.getInt(PreferencesKey.TRANSPORT_ID,1)
    private val orderId: Int = BaseApplication.getCustomPreference!!.getInt(PreferencesKey.ORDER_ID,2)
    private val vehicleCategoryLiveData = MutableLiveData<VehicleCategoryResponseModel>()
    private val vehicleResponseLiveData = MutableLiveData<AddVehicleResponseModel>()

    private var serviceId: Int = -1
    private var categoryId: Int = -1
    private var isEdit: Boolean = false
    private val addVehicleDataModel = AddVehicleDataModel()
    private val vehicleLiveData = MutableLiveData<AddVehicleDataModel>()

    private var vehicleUri: Uri? = null
    private var rcBookUri: Uri? = null
    private var insuranceUri: Uri? = null

    var loadingObservable = MutableLiveData<Boolean>()

    init {
        vehicleLiveData.value = AddVehicleDataModel()
    }

    fun getTransportId() = transportId

    fun getOrderId() = orderId

    fun setServiceId(serviceId: Int) {
        this.serviceId = serviceId
    }

    fun getServiceId() = serviceId

    fun setCategoryId(categoryId: Int) {
        this.categoryId = categoryId
    }

    fun getCategoryId() = categoryId

    fun setIsEdit(isEdit: Boolean) {
        this.isEdit = isEdit
    }

    fun getIsEdit() = isEdit


    fun setVehicleLiveData(providerVehicle: ProviderVehicleResponseModel) {
        addVehicleDataModel.vehicleImage = providerVehicle.vehicleImage
        addVehicleDataModel.vehicleModel = providerVehicle.vehicleModel
        addVehicleDataModel.vehicleYear = providerVehicle.vehicleYear.toString()
        addVehicleDataModel.vehicleColor = providerVehicle.vehicleColor
        addVehicleDataModel.vehicleNumber = providerVehicle.vehicleNo
        addVehicleDataModel.vehicleMake = providerVehicle.vehicleMake
        addVehicleDataModel.vehicleRcBook = providerVehicle.picture
        addVehicleDataModel.vehicleInsurance = providerVehicle.picture1
        addVehicleDataModel.id = providerVehicle.id
        addVehicleDataModel.vehicleId = providerVehicle.vehicleServiceId
        vehicleLiveData.value = addVehicleDataModel
    }

    fun setVehicleUri(vehicleUri: Uri) {
        this.vehicleUri = vehicleUri
    }

    fun getVehicleUri() = vehicleUri

    fun setRcBookUri(rcBookUri: Uri) {
        this.rcBookUri = rcBookUri
    }

    fun getRcBookUri() = rcBookUri

    fun setInsuranceUri(insuranceUri: Uri) {
        this.insuranceUri = insuranceUri
    }

    fun getInsuranceUri() = insuranceUri


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


    fun getVehicleCategories() {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        getCompositeDisposable().add(appRepository.getVehicleCategories(this, token))
    }

    fun postVehicle() {

        loadingObservable.value = true

        val isTransport: Boolean = serviceId == transportId

        val params = HashMap<String, RequestBody>()
        if (isTransport) {
            params[WebApiConstants.AddService.VEHICLE_ID] = createRequestBody(getVehicleData()!!.vehicleId.toString())
            params[WebApiConstants.AddService.VEHICLE_YEAR] =
                    createRequestBody(getVehicleData()!!.vehicleYear.toString())
            params[WebApiConstants.AddService.VEHICLE_MODEL] =
                    createRequestBody(getVehicleData()!!.vehicleModel.toString())
            params[WebApiConstants.AddService.VEHICLE_COLOR] =
                    createRequestBody(getVehicleData()!!.vehicleColor.toString())
        }

        params[WebApiConstants.AddService.VEHICLE_NO] = createRequestBody(getVehicleData()!!.vehicleNumber.toString())
        params[WebApiConstants.AddService.VEHICLE_MAKE] = createRequestBody(getVehicleData()!!.vehicleMake.toString())

        if (isEdit)
            params[WebApiConstants.AddService.ID] = createRequestBody(getVehicleData()!!.id.toString())
        params[WebApiConstants.AddService.CATEGORY_ID] = createRequestBody(categoryId.toString())
        params[WebApiConstants.AddService.ADMIN_SERVICE_ID] = createRequestBody(serviceId.toString())

        var vehicleMultipart: MultipartBody.Part? = null
        if (vehicleUri != null) {
            vehicleMultipart = createMultipartBody(WebApiConstants.AddService.VEHICLE_IMAGE, "image/*",
                    File(vehicleUri!!.path))
        }

        var rcBookMultipart: MultipartBody.Part? = null
        if (rcBookUri != null) {
            rcBookMultipart = createMultipartBody(WebApiConstants.AddService.PICTURE, "image/*",
                    File(rcBookUri!!.path))
        }

        var insuranceMultipart: MultipartBody.Part? = null
        if (insuranceUri != null) {
            insuranceMultipart = createMultipartBody(WebApiConstants.AddService.PICTURE1, "image/*",
                    File(insuranceUri!!.path))
        }



        if (!isEdit) {
            getCompositeDisposable().add(appRepository.postVehicle(this, params,
                    vehicleMultipart, rcBookMultipart, insuranceMultipart))
        } else {
            getCompositeDisposable().add(appRepository.editVehicle(this, params,
                    vehicleMultipart, rcBookMultipart, insuranceMultipart))
        }
    }

    fun getVehicleData() = vehicleLiveData.value

    fun getVehicleDataObservable() = vehicleLiveData

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