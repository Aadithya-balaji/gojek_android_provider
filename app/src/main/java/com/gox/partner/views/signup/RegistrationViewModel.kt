package com.gox.partner.views.signup

import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig
import com.gox.base.BuildConfig.SALT_KEY
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.repository.ApiListener
import com.gox.partner.models.CountryListResponse
import com.gox.partner.models.RegistrationResponseModel
import com.gox.partner.models.SendOTPResponse
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository
import com.gox.partner.utils.Enums
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RegistrationViewModel(private val registrationNavigator: RegistrationNavigator)
    : BaseViewModel<RegistrationViewModel.RegistrationNavigator>() {

    private val mRepository = AppRepository.instance()

    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var countryCode = MutableLiveData<String>()
    var phoneNumber = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var countryID = MutableLiveData<String>()
    var cityID = MutableLiveData<String>()
    var referralCode = MutableLiveData<String>()
    var gender = MutableLiveData<String>()
    var countryName = MutableLiveData<String>()
    var cityName = MutableLiveData<String>()
    var socialID = MutableLiveData<String>()
    var fileName = MutableLiveData<MultipartBody.Part>()
    var loginby = MutableLiveData<String>()

    var loadingProgress = MutableLiveData<Boolean>()
    var sendOTPResponse = MutableLiveData<SendOTPResponse>()
    var errorResponse = MutableLiveData<String>()


    private var countryListResponse = MutableLiveData<CountryListResponse>()
    private var mRegistrationResponse = MutableLiveData<RegistrationResponseModel>()

    init {
        this.navigator = registrationNavigator
    }

    fun getRegistrationObserverValue() = getRegistrationLiveData().value
    fun getCountryLiveData() = countryListResponse
    fun getRegistrationLiveData() = mRegistrationResponse

    fun postSignUp() {
        val signUpParams = HashMap<String, RequestBody>()
        signUpParams["salt_key"] = RequestBody.create(MediaType.parse("text/plain"), SALT_KEY)
        signUpParams[WebApiConstants.SignUp.DEVICE_TYPE] =
                RequestBody.create(MediaType.parse("text/plain"), Enums.DEVICE_TYPE)
        signUpParams[WebApiConstants.SignUp.DEVICE_TOKEN] =
                RequestBody.create(MediaType.parse("text/plain"),
                        BaseApplication.getCustomPreference!!.getString(PreferencesKey.DEVICE_TOKEN, "123")!!)
        signUpParams[WebApiConstants.SignUp.LOGIN_BY] =
                RequestBody.create(MediaType.parse("text/plain"), loginby.value.toString())
        signUpParams[WebApiConstants.SignUp.FIRST_NAME] =
                RequestBody.create(MediaType.parse("text/plain"), firstName.value.toString())
        signUpParams[WebApiConstants.SignUp.LAST_NAME] =
                RequestBody.create(MediaType.parse("text/plain"), lastName.value.toString())
        signUpParams[WebApiConstants.SignUp.EMAIL] =
                RequestBody.create(MediaType.parse("text/plain"), email.value.toString())
        signUpParams[WebApiConstants.SignUp.GENDER] =
                RequestBody.create(MediaType.parse("text/plain"), gender.value.toString())
        signUpParams[WebApiConstants.SignUp.COUNTRY_CODE] =
                RequestBody.create(MediaType.parse("text/plain"), countryCode.value.toString().replace("+",""))
        signUpParams[WebApiConstants.SignUp.MOBILE] =
                RequestBody.create(MediaType.parse("text/plain"), phoneNumber.value.toString())
        signUpParams[WebApiConstants.SignUp.PASSWORD] =
                RequestBody.create(MediaType.parse("text/plain"), password.value.toString())
        signUpParams[WebApiConstants.SignUp.COUNTRY_ID] =
                RequestBody.create(MediaType.parse("text/plain"), countryID.value.toString())
        signUpParams[WebApiConstants.SignUp.CITY_ID] =
                RequestBody.create(MediaType.parse("text/plain"), cityID.value.toString())

        if (!referralCode.value.isNullOrEmpty())
            signUpParams[WebApiConstants.SignUp.REFERRAL_CODE] =
                    RequestBody.create(MediaType.parse("text/plain"), referralCode.value.toString())
        if (!socialID.value.isNullOrEmpty())
            signUpParams[WebApiConstants.SignUp.SOCIAL_ID] =
                    RequestBody.create(MediaType.parse("text/plain"), socialID.value.toString())

        getCompositeDisposable().add(mRepository.postSignUp(object : ApiListener {
            override fun success(successData: Any) {
                getRegistrationLiveData().value = successData as RegistrationResponseModel
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        }, signUpParams, fileName.value))
    }

    fun getCountryList() {
        loadingProgress.value = true
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["salt_key"] = SALT_KEY
        getCompositeDisposable().add(mRepository.getCountryList(object : ApiListener {
            override fun success(successData: Any) {
                countryListResponse.value = successData as CountryListResponse
            }

            override fun fail(failData: Throwable) {
                loadingProgress.value = false
                gotoLogin()
            }
        }, hashMap))
    }

    fun getCityList() = registrationNavigator.getCityList()

    fun fbLogin() = registrationNavigator.facebookSignUp()

    fun googleSignIn() = registrationNavigator.googleSignUp()

    fun gotToCountryPage() = registrationNavigator.getCountryCode()

    fun getImage() = registrationNavigator.getImage()

    fun doRegistration() = registrationNavigator.validate()

    fun gotoLogin() = registrationNavigator.openSignIn()

    fun validateUser(params: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.validateUser(object : ApiListener {
            override fun success(successData: Any) {
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        }, params))
    }

    fun sendOTP() {
        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap.put("country_code", RequestBody.create(MediaType.parse("text/plain"), countryCode.value!!.replace("+", "")))
        hashMap.put("mobile", RequestBody.create(MediaType.parse("text/plain"), phoneNumber.value!!.toString()))
        hashMap.put("salt_key", RequestBody.create(MediaType.parse("text/plain"), BuildConfig.SALT_KEY))
        getCompositeDisposable().add(mRepository.sendOTP(object : ApiListener {
            override fun success(successData: Any) {
                sendOTPResponse.postValue(successData as SendOTPResponse)
            }

            override fun fail(error: Throwable) {
                errorResponse.postValue(getErrorMessage(error))
            }
        }, hashMap))
    }

    interface RegistrationNavigator {
        fun openSignIn()
        fun showError(error: String)
        fun verifyPhoneNumber()
        fun facebookSignUp()
        fun googleSignUp()
        fun getCityList()
        fun getCountryCode()
        fun getImage()
        fun validate()
    }

}