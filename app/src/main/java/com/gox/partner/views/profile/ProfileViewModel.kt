package com.gox.partner.views.profile

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.CountryListResponse
import com.gox.partner.models.ProfileResponse
import com.gox.partner.models.ResProfileUpdate
import com.gox.partner.repository.AppRepository
import com.gox.partner.utils.Constant
import com.gox.xjek.ui.profile.ProfileNavigator
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileViewModel : BaseViewModel<ProfileNavigator>() {

    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var mProfileUpdateResponse = MutableLiveData<ResProfileUpdate>()
    var errorResponse = MutableLiveData<String>()

    var mUserName: ObservableField<String> = ObservableField("")
    var mEmail: ObservableField<String> = ObservableField("")
    var mCity: ObservableField<String> = ObservableField("")
    var mCityId: ObservableField<String> = ObservableField("")
    var mCountry: ObservableField<String> = ObservableField("")
    var mCountryId: ObservableField<String> = ObservableField("")
    var mCountryCode: ObservableField<String> = ObservableField("")
    var mProfileImage: ObservableField<String> = ObservableField("")
    var mMobileNumber: ObservableField<String> = ObservableField("")

    var loadingProgress = MutableLiveData<Boolean>()
    var countryListResponse = MutableLiveData<CountryListResponse>()

    val appRepository = AppRepository.instance()

    fun getProfile() {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.getProviderProfile(this,
                Constant.M_TOKEN + readPreferences(PreferencesKey.ACCESS_TOKEN, "").toString()))
    }

    fun getProfileResponse(): MutableLiveData<ProfileResponse> {
        return mProfileResponse
    }

    fun updateProfile(file: MultipartBody.Part?) {
        loadingProgress.value = true
        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap.put("first_name", RequestBody.create(MediaType.parse("text/plain"), mUserName.get().toString()))
        //hashMap.put("mobile", RequestBody.create(MediaType.parse("text/plain"), mMobileNumber.get().toString()))
        hashMap.put("country_code", RequestBody.create(MediaType.parse("text/plain"), mCountryCode.get().toString()))
        hashMap.put("city_id", RequestBody.create(MediaType.parse("text/plain"), mCityId.get().toString()))
        // hashMap.put("country_id", RequestBody.create(MediaType.parse("text/plain"), mCountryId.get().toString()))
        getCompositeDisposable().add(appRepository
                .profileUpdate(this, Constant.M_TOKEN + readPreferences(PreferencesKey.ACCESS_TOKEN, "").toString(), hashMap, file))
    }

    fun getProfileCountryList(view: View) {
        loadingProgress.value = true

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["salt_key"] = "MQ=="

        getCompositeDisposable().add(appRepository.getCountryList(this, hashMap))
    }

    fun updateProfileResponse(): MutableLiveData<ResProfileUpdate> {
        return mProfileUpdateResponse
    }

    fun setUserName(username: String) {
        mUserName.set(username)
    }

    fun setMobileNumber(MobileNumber: String) {
        mMobileNumber.set(MobileNumber)
    }

    fun setEmail(email: String) {
        mEmail.set(email)
    }

    fun setCity(city: String) {
        mCity.set(city)
    }

    fun setCountry(country: String) {
        mCountry.set(country)
    }

    fun setCountryCode(cCode: String) {
        mCountryCode.set(cCode)
    }

    fun getCityList() {
        navigator.goToCityListActivity(mCountryId)
    }

    fun changePassord() {
        navigator.goToChangePasswordActivity()
    }

}