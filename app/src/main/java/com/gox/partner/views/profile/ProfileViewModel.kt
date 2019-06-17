package com.gox.partner.views.profile

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.CountryListResponse
import com.gox.partner.models.ProfileResponse
import com.gox.partner.models.ResProfileUpdate
import com.gox.partner.repository.AppRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
    var mProfileImage: ObservableField<String> = ObservableField("")

    var loadingProgress = MutableLiveData<Boolean>()
    var countryListResponse = MutableLiveData<CountryListResponse>()

    val mRepository = AppRepository.instance()

    fun getProfile() {
        loadingProgress.value = true
        getCompositeDisposable().add(mRepository.getProfileDetails(object : ApiListener {
            override fun success(successData: Any) {
                mProfileResponse.value = successData as ProfileResponse
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }))
    }

    fun updateProfile(file: MultipartBody.Part?) {
        loadingProgress.value = true
        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap["first_name"] = RequestBody.create(MediaType.parse("text/plain"), mUserName.get().toString())
        //hashMap.put("mobile", RequestBody.create(MediaType.parse("text/plain"), mMobileNumber.get().toString()))
        hashMap["country_code"] = RequestBody.create(MediaType.parse("text/plain"), mCountryCode.get().toString())
        hashMap["city_id"] = RequestBody.create(MediaType.parse("text/plain"), mCityId.get().toString())
        // hashMap.put("country_id", RequestBody.create(MediaType.parse("text/plain"), mCountryId.get().toString()))
        getCompositeDisposable().add(mRepository.profileUpdate(object : ApiListener {
            override fun success(successData: Any) {
                mProfileUpdateResponse.value = successData as ResProfileUpdate
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }, hashMap, file))
    }

    fun getProfileCountryList(view: View) {
        loadingProgress.value = true

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

    fun updateProfileResponse() = mProfileUpdateResponse

    fun getCityList() = navigator.goToCityListActivity(mCountryId)

    fun changePassword() = navigator.goToChangePasswordActivity()

}