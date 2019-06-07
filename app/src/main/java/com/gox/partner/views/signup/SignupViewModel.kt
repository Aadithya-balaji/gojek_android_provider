package com.gox.partner.views.signup

import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig.SALT_KEY
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.partner.models.CountryListResponse
import com.gox.partner.models.SignupResponseModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository
import com.gox.partner.utils.Enums
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SignupViewModel(val signupNavigator: SignupNavigator) : BaseViewModel<SignupViewModel.SignupNavigator>() {

    private val appRepository = AppRepository.instance()

    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var countryCode = MutableLiveData<String>()
    var phoneNumber = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var confirmPassword = MutableLiveData<String>()
    var countryID = MutableLiveData<String>()
    var cityID = MutableLiveData<String>()
    var referralCode = MutableLiveData<String>()
    var stateID = MutableLiveData<String>()
    var gender = MutableLiveData<String>()
    var countryName = MutableLiveData<String>()
    var cityName = MutableLiveData<String>()
    var socialID = MutableLiveData<String>()
    var fileName = MutableLiveData<MultipartBody.Part>()
    var loginby = MutableLiveData<String>()
    private var countryListResponse = MutableLiveData<CountryListResponse>()
    private var signupResponse = MutableLiveData<SignupResponseModel>()
    private var errorResponse = MutableLiveData<Throwable>()

    init {
        this.navigator = signupNavigator
    }

    fun doRegistration() {
        signupNavigator.validate()
    }

    fun gotoSignin() {
        signupNavigator.openSignin()
    }

    fun gotoDocumentPage() {
        signupNavigator.gotoDocumentPage()
    }

    fun getSignupObserverValue() = getSignupLiveData().value
    fun getCountryLiveData() = countryListResponse
    fun getSignupLiveData() = signupResponse


    fun postSignup() {
        val signupParams = HashMap<String, RequestBody>()
        signupParams.put("salt_key", RequestBody.create(MediaType.parse("text/plain"), SALT_KEY))
        signupParams.put(WebApiConstants.Signup.DEVICE_TYPE, RequestBody.create(MediaType.parse("text/plain"), Enums.DEVICE_TYPE))
        signupParams.put(WebApiConstants.Signup.DEVICE_TOKEN, RequestBody.create(MediaType.parse("text/plain"), BaseApplication.getCustomPreference!!.getString(PreferencesKey.DEVICE_TOKEN, "123")!!))
        signupParams.put(WebApiConstants.Signup.LOGIN_BY, RequestBody.create(MediaType.parse("text/plain"), loginby.value.toString()))
        signupParams.put(WebApiConstants.Signup.FIRST_NAME, RequestBody.create(MediaType.parse("text/plain"), firstName.value.toString()))
        signupParams.put(WebApiConstants.Signup.LAST_NAME, RequestBody.create(MediaType.parse("text/plain"), lastName.value.toString()))
        signupParams.put(WebApiConstants.Signup.EMAIL, RequestBody.create(MediaType.parse("text/plain"), email.value.toString()))
        signupParams.put(WebApiConstants.Signup.GENDER, RequestBody.create(MediaType.parse("text/plain"), gender.value.toString()))
        signupParams.put(WebApiConstants.Signup.COUNTRY_CODE, RequestBody.create(MediaType.parse("text/plain"), countryCode.value.toString()))
        signupParams.put(WebApiConstants.Signup.MOBILE, RequestBody.create(MediaType.parse("text/plain"), phoneNumber.value.toString()))
        signupParams.put(WebApiConstants.Signup.PASSWORD, RequestBody.create(MediaType.parse("text/plain"), password.value.toString()))
        signupParams.put(WebApiConstants.Signup.COUNTRY_ID, RequestBody.create(MediaType.parse("text/plain"), countryID.value.toString()))
        signupParams.put(WebApiConstants.Signup.CITY_ID, RequestBody.create(MediaType.parse("text/plain"), cityID.value.toString()))
        if (!referralCode.value.isNullOrEmpty())
            signupParams.put(WebApiConstants.Signup.REFERRAL_CODE, RequestBody.create(MediaType.parse("text/plain"), referralCode.value.toString()))
        if (!socialID.value.isNullOrEmpty())
            signupParams.put(WebApiConstants.Signup.SOCIAL_ID, RequestBody.create(MediaType.parse("text/plain"), socialID.value.toString()))

        getCompositeDisposable().add(appRepository.postSignup(this, signupParams, fileName.value))

    }

    fun getCountryList() {
        // loadingProgress.value=true
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["salt_key"] = SALT_KEY
        getCompositeDisposable().add(appRepository.getCountryList(this, hashMap))
    }

    fun getCityList() {
        signupNavigator.getCityList()
    }

    fun fbLogin() {
        signupNavigator.facebookSignup()
    }

    fun googleSignin() {
        signupNavigator.googleSignup()
    }


    fun gotToCountryPage() {
        signupNavigator.getCountryCode()
    }

    fun getImage() {
        signupNavigator.getImage()
    }

    fun validateUser(params: HashMap<String, String>) {
        getCompositeDisposable().add(appRepository.ValidateUser(this, params))
    }

    interface SignupNavigator {
        fun signup()
        fun openSignin()
        fun gotoDocumentPage()
        fun showError(error: String)
        fun verifyPhoneNumber()
        fun facebookSignup()
        fun googleSignup()
        fun getCityList()
        fun getCountryCode()
        fun getImage()
        fun validate()
    }

}