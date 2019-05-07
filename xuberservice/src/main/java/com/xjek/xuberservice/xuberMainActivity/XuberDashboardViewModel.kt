package com.xjek.xuberservice.xuberMainActivity

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.Constants.Common.ID
import com.xjek.base.data.Constants.Common.METHOD
import com.xjek.base.data.Constants.XuperProvider.STATUS
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.xuberservice.model.CancelRequestModel
import com.xjek.xuberservice.model.UpdateRequest
import com.xjek.xuberservice.model.XuperCheckRequest
import com.xjek.xuberservice.repositary.XuperRepoitory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class XuberDashboardViewModel : BaseViewModel<XuberDasbBoardNavigator>() {

    val xuperRepository = XuperRepoitory.instance()
    var driverStatus: ObservableField<String> = ObservableField("Driver accepted your request")
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var showLoading = MutableLiveData<Boolean>()
    var xuperCheckRequest = MutableLiveData<XuperCheckRequest>()
    var xuperUdpateRequest = MutableLiveData<UpdateRequest>()
    var xuperCancelRequest = MutableLiveData<CancelRequestModel>()
    var userRating = MutableLiveData<String>()
    var userImage = MutableLiveData<String>()
    var serviceType = MutableLiveData<String>()
    var otp = MutableLiveData<String>()
    var polyLineSrc = MutableLiveData<LatLng>()
    var polyLineDest = MutableLiveData<LatLng>()
    var currentStatus = MutableLiveData<String>()

    fun showInfoDialog(view:View) {
        navigator.showInfoWindow(view)
    }

    fun pickLocation() {
        navigator.goToLocationPick()
    }

    fun goBack() {
        navigator.goBack()
    }

    fun onClickStatus(view: View) {
        navigator.updateService(view)
    }

    fun showCamPage() {
        navigator.showPicturePreview()
    }

    fun showCurrentLocation() {
        navigator.showCurrentLocation()
    }

    fun moveStatusFlow() {
        navigator.moveStatusFlow()
    }

    fun setDriverStatus(status: String) {
        driverStatus.set(status)
        driverStatus.notifyChange()
    }

    fun callXuperCheckRequest() {
        // showLoading.value=true
        getCompositeDisposable().add(xuperRepository.xuperCheckRequesst
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), latitude.value.toString(), longitude.value.toString()))
    }

    fun updateRequest(status: String, file: MultipartBody.Part?, isFrontImage: Boolean) {
        val params = HashMap<String, RequestBody>()
        params[ID] = RequestBody.create(MediaType.parse("text/plain"), xuperCheckRequest.value!!.responseData!!.requests!!.id.toString())
        params[STATUS] = RequestBody.create(MediaType.parse("text/plain"), status)
        params[METHOD] = RequestBody.create(MediaType.parse("text/plain"), "PATCH")
        if (isFrontImage) {
            getCompositeDisposable().add(xuperRepository.xuperUpdateRequest(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params, file, null))
        } else {
            getCompositeDisposable().add(xuperRepository.xuperUpdateRequest(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params, null, file))

        }
    }

    fun cancelRequest(params: HashMap<String, String>) {
        showLoading.value = true
        getCompositeDisposable().add(xuperRepository.xuperCancelRequest(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
    }
}