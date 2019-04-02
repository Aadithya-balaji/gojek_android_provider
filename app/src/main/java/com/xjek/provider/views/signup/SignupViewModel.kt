package com.xjek.provider.views.signup

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.CityListResponse
import com.xjek.provider.models.SignupResponseModel
import com.xjek.provider.models.StateListResponse
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Enums
import com.xjek.user.data.repositary.remote.model.CountryListResponse

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
    var stateID = MutableLiveData<String>()
    var gender = MutableLiveData<String>()
    var countryName = MutableLiveData<String>()
    var cityName = MutableLiveData<String>()


    private var countryListResponse = MutableLiveData<CountryListResponse>()
    private var signupResponse = MutableLiveData<SignupResponseModel>()
    private var stateListResponse = MutableLiveData<StateListResponse>()
    private var cityListResponse = MutableLiveData<CityListResponse>()
    private var errorResponse = MutableLiveData<Throwable>()
    var loadingProgress = MutableLiveData<Boolean>()

    fun doRegistration() {
        signupNavigator.validate()
    }

    fun gotoSignin() {
        signupNavigator.openSignin()
    }

    fun gotoDocumentPage() {
        signupNavigator.gotoDocumentPage()
    }

    fun getCountryObserverValue() = getCountryLiveData().value
    fun getStateListObserverValue() = getStateLiveData().value
    fun getCityListObserverValue() = getCityLiveData().value
    fun getSignupObserverValue() = getSignupLiveData().value

    fun getCountryLiveData() = countryListResponse
    fun getStateLiveData() = stateListResponse
    fun getCityLiveData() = cityListResponse
    fun getSignupLiveData() = signupResponse


    fun postSignup() {
        val signupParams = HashMap<String, String>()
        signupParams.put(WebApiConstants.SALT_KEY, "MQ==")
        signupParams.put(WebApiConstants.DEVICE_TYPE, Enums.DEVICE_TYPE)
        signupParams.put(WebApiConstants.DEVICE_TOKEN, "123456")
        signupParams.put(WebApiConstants.LOGIN_BY, Enums.LOGINBY.MANUAL.name.toUpperCase())
        signupParams.put(WebApiConstants.FIRST_NAME, firstName.value.toString())
        signupParams.put(WebApiConstants.LAST_NAME, lastName.value.toString())
        signupParams.put(WebApiConstants.EMAIL, email.value.toString())
        signupParams.put(WebApiConstants.GENDER, gender.value.toString())
        signupParams.put(WebApiConstants.COUNTRY_CODE, countryCode.value.toString())
        signupParams.put(WebApiConstants.MOBILE, phoneNumber.value.toString())
        signupParams.put(WebApiConstants.PASSWORD, password.value.toString())
        signupParams.put(WebApiConstants.CONFIRM_PASSWORD, confirmPassword.value.toString())
        signupParams.put(WebApiConstants.COUNTRY_ID, countryID.value.toString())
        signupParams.put(WebApiConstants.CITY_ID, cityID.value.toString())
        signupParams.put(WebApiConstants.STATE_ID, stateID.value.toString())

        getCompositeDisposable().add(appRepository.postSignup(this, signupParams))
    }

    fun getCountryList() {
        // loadingProgress.value=true
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["salt_key"] = "MQ=="
        getCompositeDisposable().add(appRepository.getCountryList(this, hashMap))
    }

    fun getStateList() {
        //getCompositeDisposable().add(appRepository.getStateList(this, stateID.value!!))
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

    fun validate() {
        val signupNavigator1 = validate()
    }

    fun getCountryCode() {
        signupNavigator.getCountryCode()
    }

    fun getImage() {
        signupNavigator.getImage()
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