package com.xjek.provider.views.signup

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.SignupResponseModel
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Enums
import com.xjek.provider.models.CityListResponse
import com.xjek.provider.models.CountryListResponse
import com.xjek.provider.models.StateListResponse

class SignupViewModel(val signupNavigator: SignupNavigator) : BaseViewModel<SignupViewModel.SignupNavigator>() {

    private val appRepository = AppRepository.instance()

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val countryCode = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    val countryID = MutableLiveData<String>()
    val cityID = MutableLiveData<String>()
    val stateID = MutableLiveData<String>()
    val gender = MutableLiveData<String>()


    private var countryListResponse = MutableLiveData<CountryListResponse>()
    private var signupResponse = MutableLiveData<SignupResponseModel>()
    private var stateListResponse = MutableLiveData<StateListResponse>()
    private var cityListResponse = MutableLiveData<CityListResponse>()
    private var errorResponse = MutableLiveData<Throwable>()
    var loadingProgress = MutableLiveData<Boolean>()

    fun doRegistration() {
        signupNavigator.verifyPhoneNumber()
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
        //getCompositeDisposable().add(appRepository.getCountryList(this, countryID.value!!))
    }

    fun getStateList() {
        //getCompositeDisposable().add(appRepository.getStateList(this, stateID.value!!))
    }

    fun getCityList() {
       // getCompositeDisposable().add(appRepository.getCityList(this, cityID.value!!))
    }

    fun fbLogin(){
        signupNavigator.facebookSignup()
    }

    fun googleSignin(){
        signupNavigator.googleSignup()
    }


    interface SignupNavigator {
        fun signup()
        fun openSignin()
        fun gotoDocumentPage()
        fun showError(error: String)
        fun verifyPhoneNumber()
        fun facebookSignup()
        fun  googleSignup()
    }

}