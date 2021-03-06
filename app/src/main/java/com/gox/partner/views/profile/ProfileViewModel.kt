package com.gox.partner.views.profile

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.*
import com.gox.partner.repository.AppRepository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileViewModel : BaseViewModel<ProfileNavigator>() {

    var mProfileUpdateResponse = MutableLiveData<ResProfileUpdate>()
    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var errorResponse = MutableLiveData<String>()

    var mCity: ObservableField<String> = ObservableField("")
    var mEmail: ObservableField<String> = ObservableField("")
    var mCityId: ObservableField<String> = ObservableField("")
    var mCountry: ObservableField<String> = ObservableField("")
    var mUserName: ObservableField<String> = ObservableField("")
    var mCountryId: ObservableField<String> = ObservableField("")
    var mCountryCode: ObservableField<String> = ObservableField("")
    var mMobileNumber: ObservableField<String> = ObservableField("")

    var showLoading = MutableLiveData<Boolean>()
    var countryListResponse = MutableLiveData<CountryListResponse>()
    var countryResponse = MutableLiveData<CountryResponseData>()
    var sendOTPResponse = MutableLiveData<SendOTPResponse>()

    val mRepository = AppRepository.instance()

    fun getProfile() {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.getProfileDetails(object : ApiListener {
            override fun success(successData: Any) {
                mProfileResponse.value = successData as ProfileResponse
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }))
    }

    fun updateProfile(file: MultipartBody.Part?) {
        showLoading.value = true
        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap["first_name"] = mUserName.get().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        hashMap["mobile"] = mMobileNumber.get().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        hashMap["country_code"] = mCountryCode.get().toString().replace("+","").toRequestBody("text/plain".toMediaTypeOrNull())
        hashMap["city_id"] = mCityId.get().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        // hashMap.put("country_id", RequestBody.create(MediaType.parse("text/plain"), mCountryId.get().toString()))
        getCompositeDisposable().add(mRepository.profileUpdate(object : ApiListener {
            override fun success(successData: Any) {
                mProfileUpdateResponse.value = successData as ResProfileUpdate
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, hashMap, file))
    }

    fun getProfileCountryList() {
        showLoading.value = true

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["salt_key"] = BuildConfig.SALT_KEY

        getCompositeDisposable().add(mRepository.getCountryList(object : ApiListener {
            override fun success(successData: Any) {
                countryListResponse.value = successData as CountryListResponse
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
            }
        }, hashMap))
    }

    fun getProfileCountryCityList() {
        showLoading.value = true

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["salt_key"] = BuildConfig.SALT_KEY

        getCompositeDisposable().add(mRepository.getCountryList(object : ApiListener {
            override fun success(successData: Any) {
                 val countryResponseData = successData as CountryListResponse
                 countryResponse.value = countryResponseData.responseData.find { mCountryId.get() == it.id.toString() }
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
            }
        }, hashMap))
    }

    fun updateProfileResponse() = mProfileUpdateResponse

    fun getCityList() = navigator.goToCityListActivity(mCountryId)

    fun changePassword() = navigator.goToChangePasswordActivity()

    fun sendOTP() {
        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap.put("country_code", mCountryCode.get().toString().replace("+","").toRequestBody("text/plain".toMediaTypeOrNull()))
        hashMap.put("mobile", mMobileNumber.get().toString().toRequestBody("text/plain".toMediaTypeOrNull()))
        hashMap.put("salt_key", BuildConfig.SALT_KEY.toRequestBody("text/plain".toMediaTypeOrNull()))
        getCompositeDisposable().add(mRepository.sendOTP(object : ApiListener {
            override fun success(successData: Any) {
                sendOTPResponse.postValue(successData as SendOTPResponse)
            }

            override fun fail(error: Throwable) {
                errorResponse.postValue(getErrorMessage(error))
            }
        }, hashMap))
    }

}