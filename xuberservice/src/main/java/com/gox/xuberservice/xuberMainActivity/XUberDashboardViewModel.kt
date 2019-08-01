package com.gox.xuberservice.xuberMainActivity

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants.Common.ID
import com.gox.base.data.Constants.Common.METHOD
import com.gox.base.data.Constants.XUberProvider.EXTRA_CHARGE
import com.gox.base.data.Constants.XUberProvider.EXTRA_CHARGE_NOTES
import com.gox.base.data.Constants.XUberProvider.STATUS
import com.gox.base.repository.ApiListener
import com.gox.xuberservice.model.CancelRequestModel
import com.gox.xuberservice.model.UpdateRequest
import com.gox.xuberservice.model.XuperCheckRequest
import com.gox.xuberservice.repositary.XUberRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class XUberDashboardViewModel : BaseViewModel<XUberDashBoardNavigator>() {

    private val mRepository = XUberRepository.instance()

    var xUberCancelRequest = MutableLiveData<CancelRequestModel>()
    var xUberCheckRequest = MutableLiveData<XuperCheckRequest>()
    var xUberUpdateRequest = MutableLiveData<UpdateRequest>()

    var currentStatus = MutableLiveData<String>()
    var serviceType = MutableLiveData<String>()
    var userRating = MutableLiveData<String>()
    var descImage = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var strDesc = MutableLiveData<String>()
    var otp = MutableLiveData<String>()

    var showLoading = MutableLiveData<Boolean>()
    var longitude = MutableLiveData<Double>()
    var latitude = MutableLiveData<Double>()

    var polyLineSrc = MutableLiveData<LatLng>()
    var tempDest = MutableLiveData<LatLng>()
    var tempSrc = MutableLiveData<LatLng>()

    fun showInfoDialog(view: View) = navigator.showInfoWindow(view)

    fun onClickStatus(view: View) = navigator.updateService(view)

    fun callXUberCheckRequest() {
        if (BaseApplication.isNetworkAvailable) {
            getCompositeDisposable().add(mRepository.xUberCheckRequest
            (object : ApiListener {
                override fun success(successData: Any) {
                    xUberCheckRequest.value = successData as XuperCheckRequest
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }, latitude.value.toString(), longitude.value.toString()))
        }
    }

    fun updateRequest(status: String, file: MultipartBody.Part?, isFrontImage: Boolean, extraChargeNotes: String, extraCharge: String) {
        if (BaseApplication.isNetworkAvailable) {
            showLoading.value = true
            val params = HashMap<String, RequestBody>()
            params[ID] = RequestBody.create(MediaType.parse("text/plain"),
                    xUberCheckRequest.value!!.responseData!!.requests!!.id.toString())
            params[STATUS] = RequestBody.create(MediaType.parse("text/plain"), status)
            params[METHOD] = RequestBody.create(MediaType.parse("text/plain"), "PATCH")
            params[EXTRA_CHARGE] = RequestBody.create(MediaType.parse("text/plain"), extraCharge)
            params[EXTRA_CHARGE_NOTES] = RequestBody.create(MediaType.parse("text/plain"), extraChargeNotes)

            if (isFrontImage) getCompositeDisposable().add(mRepository.xUberUpdateRequest(object : ApiListener {
                override fun success(successData: Any) {
                    xUberUpdateRequest.value = successData as UpdateRequest
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }, params, file, null))
            else getCompositeDisposable().add(mRepository.xUberUpdateRequest(object : ApiListener {
                override fun success(successData: Any) {
                    xUberUpdateRequest.value = successData as UpdateRequest
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }, params, null, file))
        }
    }

    fun cancelRequest(params: HashMap<String, String>) {
        if (BaseApplication.isNetworkAvailable) {
            showLoading.value = true
            getCompositeDisposable().add(mRepository.xUberCancelRequest(object : ApiListener {
                override fun success(successData: Any) {
                    xUberCancelRequest.value = successData as CancelRequestModel
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }, params))
        }
    }
}