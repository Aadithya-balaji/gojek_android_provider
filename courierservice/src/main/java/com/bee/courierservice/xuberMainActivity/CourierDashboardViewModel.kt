package com.bee.courierservice.xuberMainActivity

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
import com.bee.courierservice.model.CancelRequestModel
import com.bee.courierservice.model.UpdateRequest
import com.bee.courierservice.model.CourierCheckRequest
import com.bee.courierservice.repositary.CourierRepository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CourierDashboardViewModel : BaseViewModel<CourierDashBoardNavigator>() {

    private val mRepository = CourierRepository.instance()

    var xUberCancelRequest = MutableLiveData<CancelRequestModel>()
    var xUberCheckRequest = MutableLiveData<CourierCheckRequest>()
    var xUberUpdateRequest = MutableLiveData<CourierCheckRequest>()

    var currentStatus = MutableLiveData<String>()
    var mainCurrentStatus = MutableLiveData<String>()
    var serviceType = MutableLiveData<String>()
    var userRating = MutableLiveData<String>()
    var descImage = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var strDesc = MutableLiveData<String>()
    var weight = MutableLiveData<String>()
    var length = MutableLiveData<String>()
    var height = MutableLiveData<String>()
    var width = MutableLiveData<String>()
    var otp = MutableLiveData<String>()

    var showLoading = MutableLiveData<Boolean>()
    var longitude = MutableLiveData<Double>()
    var latitude = MutableLiveData<Double>()

    var polyLineSrc = MutableLiveData<LatLng>()
    var polyLineDest = MutableLiveData<LatLng>()
    var tempDest = MutableLiveData<LatLng>()
    var tempSrc = MutableLiveData<LatLng>()

    var iteratePointsForApi = ArrayList<LatLng>()

    fun showInfoDialog(view: View) = navigator.showInfoWindow(view)

    fun onClickStatus(view: View) = navigator.updateService(view)

    fun callXUberCheckRequest() {
        if (BaseApplication.isNetworkAvailable) {
            getCompositeDisposable().add(mRepository.xUberCheckRequest
            (object : ApiListener {
                override fun success(successData: Any) {
                    xUberCheckRequest.value = successData as CourierCheckRequest
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }, latitude.value.toString(), longitude.value.toString()))
        }
    }

    fun updateRequest(status: String, file: MultipartBody.Part?, isFrontImage: Boolean, extraChargeNotes: String, extraCharge: String,postion:Int) {
        if (BaseApplication.isNetworkAvailable) {
            showLoading.value = true
            val params = HashMap<String, RequestBody>()
            params[ID] = xUberCheckRequest.value!!.responseData.request.delivery.id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            params[STATUS] = status.toRequestBody("text/plain".toMediaTypeOrNull())
            params[METHOD] = "PATCH".toRequestBody("text/plain".toMediaTypeOrNull())
            params[EXTRA_CHARGE] = extraCharge.toRequestBody("text/plain".toMediaTypeOrNull())
            params[EXTRA_CHARGE_NOTES] = extraChargeNotes.toRequestBody("text/plain".toMediaTypeOrNull())

            if (isFrontImage) getCompositeDisposable().add(mRepository.xUberUpdateRequest(object : ApiListener {
                override fun success(successData: Any) {
                    xUberUpdateRequest.value = successData as CourierCheckRequest
                    showLoading.postValue(false)
                }
                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }, params, file, null))
            else getCompositeDisposable().add(mRepository.xUberUpdateRequest(object : ApiListener {
                override fun success(successData: Any) {
                    xUberUpdateRequest.value = successData as CourierCheckRequest
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