package com.gox.partner.views.add_vehicle

import android.net.Uri
import android.os.FileUtils
import android.util.Log
import android.view.View
import androidx.databinding.BindingConversion
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.room.util.FileUtil
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.createMultipartBody
import com.gox.base.extensions.createRequestBody
import com.gox.base.repository.ApiListener
import com.gox.partner.models.AddVehicleDataModel
import com.gox.partner.models.AddVehicleResponseModel
import com.gox.partner.models.ProviderVehicleResponseModel
import com.gox.partner.models.VehicleCategoryResponseModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AddVehicleViewModel : BaseViewModel<AddVehicleNavigator>() {

    private val mRepository = AppRepository.instance()

    private val transportServiceName: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.TRANSPORT_ID, Constants.ModuleTypes.TRANSPORT) ?: ""
    private val deliveryServiceName: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.DELIVERY_ID, Constants.ModuleTypes.DELIVERY) ?: ""
    private val orderServiceName: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.ORDER_ID, Constants.ModuleTypes.ORDER) ?: ""
    private val vehicleCategoryLiveData = MutableLiveData<VehicleCategoryResponseModel>()
    private val vehicleResponseLiveData = MutableLiveData<AddVehicleResponseModel>()
    var specialSeatLiveData = MutableLiveData<Boolean>()

    private var serviceName: String = Constants.ModuleTypes.TRANSPORT
    private var categoryId: Int = -1
    private var serviceStatus: String = ""
    private var isEdit: Boolean = false
    private val addVehicleDataModel = AddVehicleDataModel()
    private val vehicleLiveData = MutableLiveData<AddVehicleDataModel>()
    var isEditAble = ObservableField<Boolean>()

//    private var isEditAble = true //initializing with empty string, so that it doesn't crash


//    var isEditAble = true;
    private var vehicleUri: Uri? = null
    private var rcBookUri: Uri? = null
    private var insuranceUri: Uri? = null

    var showLoading = MutableLiveData<Boolean>()

    init {
        vehicleLiveData.value = AddVehicleDataModel()
    }

    fun getTransportServiceName() = transportServiceName
    fun getDeliveryServiceName() = deliveryServiceName

    fun setServiceName(serviceName: String) {
        this.serviceName = serviceName
    }

    fun getServiceName() = serviceName


    fun setServiceStatus(serviceStatus: String) {
        this.serviceStatus = serviceStatus
    }

    fun getServiceStatus() = serviceStatus

    fun setCategoryId(categoryId: Int) {
        this.categoryId = categoryId
    }

    fun setIsEdit(isEdit: Boolean) {
        this.isEdit = isEdit
    }

//    @BindingConversion
//    fun getRememberMe() = isEditAble
//
//    fun setRememberMe(value: Boolean) {
//        isEditAble = value    }

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
        addVehicleDataModel.wheelChair = providerVehicle.wheelChair
        addVehicleDataModel.childSeat = providerVehicle.childSeat
        vehicleLiveData.value = addVehicleDataModel
    }

    fun setVehicleUri(vehicleUri: Uri) {
        this.vehicleUri = vehicleUri
    }

    fun setRcBookUri(rcBookUri: Uri) {
        this.rcBookUri = rcBookUri
    }

    fun getRcBookUri() = rcBookUri

    fun setInsuranceUri(insuranceUri: Uri) {
        this.insuranceUri = insuranceUri
    }

    fun getInsuranceUri() = insuranceUri

    fun isFieldMandatory() = when (serviceName) {
        transportServiceName -> true
        deliveryServiceName -> true
        orderServiceName -> false
        else -> false
    }

//    fun isEditAble() = when (serviceStatus) {
//        "INACTIVE" -> false
//        "ACTIVE" -> false
//         else -> true
//    }

    fun postVehicle() {
        showLoading.value = true
        val isTransport: Boolean = serviceName == transportServiceName
        val isDelivery: Boolean = serviceName == deliveryServiceName
        val params = HashMap<String, RequestBody>()
        if (isTransport || isDelivery) {
            params[WebApiConstants.AddService.VEHICLE_ID] = createRequestBody(getVehicleData()!!.vehicleId.toString())
            params[WebApiConstants.AddService.VEHICLE_YEAR] =
                    createRequestBody(getVehicleData()!!.vehicleYear!!)
            params[WebApiConstants.AddService.VEHICLE_MODEL] =
                    createRequestBody(getVehicleData()!!.vehicleModel!!)
            params[WebApiConstants.AddService.VEHICLE_COLOR] =
                    createRequestBody(getVehicleData()!!.vehicleColor!!)
        }

        if (specialSeatLiveData.value!! && getVehicleData()!!.childSeat == 1) {
            params[WebApiConstants.AddService.CHILD_SEAT] = createRequestBody(getVehicleData()!!.childSeat.toString())
        }
        if (specialSeatLiveData.value!! && getVehicleData()!!.wheelChair == 1) {
            params[WebApiConstants.AddService.WHEEL_CHAIR] = createRequestBody(getVehicleData()!!.wheelChair.toString())
        }


        params[WebApiConstants.AddService.VEHICLE_NO] = createRequestBody(getVehicleData()!!.vehicleNumber!!)
        params[WebApiConstants.AddService.VEHICLE_MAKE] = createRequestBody(getVehicleData()!!.vehicleMake!!)

        if (isEdit)
            params[WebApiConstants.AddService.ID] = createRequestBody(getVehicleData()!!.id.toString())
        params[WebApiConstants.AddService.CATEGORY_ID] = createRequestBody(categoryId.toString())
        params[WebApiConstants.AddService.ADMIN_SERVICE] = createRequestBody(serviceName.toString())

        var vehicleMultipart: MultipartBody.Part? = null
        if (vehicleUri != null) vehicleMultipart =
                createMultipartBody(WebApiConstants.AddService.VEHICLE_IMAGE, "image/*",
                        File(vehicleUri!!.path))

        var rcBookMultipart: MultipartBody.Part? = null
        if (rcBookUri != null) rcBookMultipart =
                createMultipartBody(WebApiConstants.AddService.PICTURE, "image/*",
                        File(rcBookUri!!.path))

        var insuranceMultipart: MultipartBody.Part? = null
        if (insuranceUri != null) insuranceMultipart =
                createMultipartBody(WebApiConstants.AddService.PICTURE1, "image/*",
                        File(insuranceUri!!.path))

        if (!isEdit) getCompositeDisposable().add(mRepository.postVehicle(object : ApiListener {
            override fun success(successData: Any) {
                showLoading.postValue(false)
                getVehicleResponseObservable().value = successData as AddVehicleResponseModel
            }

            override fun fail(failData: Throwable) {
                showLoading.postValue(false)
                navigator.showError(getErrorMessage(failData))
            }
        }, params, vehicleMultipart, rcBookMultipart, insuranceMultipart))
        else getCompositeDisposable().add(mRepository.editVehicle(object : ApiListener {
            override fun success(successData: Any) {
                getVehicleResponseObservable().value = successData as AddVehicleResponseModel
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
                showLoading.postValue(false)
            }
        }, params, vehicleMultipart, rcBookMultipart, insuranceMultipart))
    }

    fun getVehicleData() = vehicleLiveData.value

    fun getVehicleDataObservable() = vehicleLiveData

    fun getVehicleCategoryObservable() = vehicleCategoryLiveData

    fun getVehicleResponseObservable() = vehicleResponseLiveData

    fun onVehicleImageClick(view: View) = navigator.onVehicleImageClicked()

    fun onRcBookClick(view: View) = navigator.onRcBookClicked()

    fun onInsuranceClick(view: View) = navigator.onInsuranceClicked()

    fun onVehicleSubmitClick(view: View) = navigator.onVehicleSubmitClicked()

    fun onChildSeatCheckChanged(isChecked:Boolean){
        if(isChecked){
            getVehicleData()?.childSeat = 1
        }else{
            getVehicleData()?.childSeat = 0
        }
    }

    fun onWheelChairCheckChanged(isChecked:Boolean){
        if(isChecked){
            getVehicleData()?.wheelChair = 1
        }else{
            getVehicleData()?.wheelChair = 0
        }
    }

}