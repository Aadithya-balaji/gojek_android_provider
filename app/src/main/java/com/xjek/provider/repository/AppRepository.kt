package com.xjek.provider.repository

import android.annotation.SuppressLint
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.base.repository.BaseRepository
import com.xjek.provider.network.AppWebService
import com.xjek.provider.views.add_vehicle.AddVehicleViewModel
import com.xjek.provider.views.change_password.ChangePasswordViewModel
import com.xjek.provider.views.forgot_password.ForgotPasswordViewModel
import com.xjek.provider.views.invitereferals.InviteReferalsViewModel
import com.xjek.provider.views.manage_services.ManageServicesViewModel
import com.xjek.provider.views.profile.ProfileViewModel
import com.xjek.provider.views.reset_password.ResetPasswordViewModel
import com.xjek.provider.views.setup_vehicle.SetupVehicleViewModel
import com.xjek.provider.views.sign_in.SignInViewModel
import com.xjek.provider.views.signup.SignupViewModel
import com.xjek.provider.views.splash.SplashViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import java.util.logging.Level
import java.util.logging.Logger

class AppRepository : BaseRepository() {

    private val serviceId: String
        get() = PreferencesHelper.get<Int>(PreferencesKey.BASE_ID).toString()

    @SuppressLint("CheckResult")
    fun getConfig(viewModel: SplashViewModel, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(AppWebService::class.java)
                .getConfig(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getConfigObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    @SuppressLint("CheckResult")
    fun postLogin(viewModel: SignInViewModel, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postLogin(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getLoginObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    @SuppressLint("CheckResult")
    fun postSocialLogin(viewModel: SignInViewModel, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postSocialLogin(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getLoginObservable().postValue(it)
                }, {
                    viewModel.navigator.showAlert(getErrorMessage(it))
                })
    }

    @SuppressLint("CheckResult")
    fun postForgotPassword(viewModel: ForgotPasswordViewModel, params: HashMap<String, String>):
            Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postForgotPassword(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getForgotPasswordObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    @SuppressLint("CheckResult")
    fun postResetPassword(viewModel: ResetPasswordViewModel, params: HashMap<String, String>):
            Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postResetPassword(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getResetPasswordObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    @SuppressLint("CheckResult")
    fun getCountryList(viewModel: SignupViewModel, params: HashMap<String, Any?>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getCountries(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getCountryLiveData().postValue(it)
                }, {
                    Logger.getLogger(TAG).log(Level.SEVERE, it.message)
                    viewModel.gotoSignin()
                })
    }


    @SuppressLint("CheckResult")
    fun postSignup(viewModel: SignupViewModel, params: HashMap<String, RequestBody>,
                   @Part filename: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postSignup(params, filename)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getSignupLiveData().postValue(it)
                }, {

                    viewModel.signupNavigator.showError(getErrorMessage(it))
                })
    }

    fun postChangePassword(viewModel: ChangePasswordViewModel, token: String,
                           params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postChangePassword(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getChangePasswordObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun ValidateUser(viewModel: SignupViewModel, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .verifyUser(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200") {

                    }
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })

    }

    fun getProfile(viewModel: ProfileViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getProfile(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200") {
                        viewModel.getProfileRespose().postValue(it)
                    }
                }, {
                    viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })
    }

    fun getReferal(viewModel: InviteReferalsViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getProfile(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getProfileLiveData().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun updateProfile(viewModel: ProfileViewModel, param: HashMap<String, RequestBody>,
                      @Part filename: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .updateProfile(param, filename)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.updateProfileResposne.postValue(it)
                }, {
                    viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })
    }

    fun getServices(viewModel: ManageServicesViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getServices(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getServicesObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun getRides(viewModel: SetupVehicleViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getRides(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getVehicleDataObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun getShops(viewModel: SetupVehicleViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getShops(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getVehicleDataObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun getVehicleCategories(viewModel: AddVehicleViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getVehicleCategories(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getVehicleCategoryObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun postVehicle(viewModel: AddVehicleViewModel, token: String,
                    params: HashMap<String, RequestBody>,
                    rcBookMultipart: MultipartBody.Part, insuranceMultipart: MultipartBody.Part
    ): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postVehicle(token, params, rcBookMultipart, insuranceMultipart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getVehicleResponseObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

//    fun getServices(viewModel: SetupVehicleViewModel, token: String): Disposable {
//        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
//                .getRides(token)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe({
//                    if (it.statusCode == "200")
//                        viewModel.getVehicleDataObservable().postValue(it)
//                }, {
//                    viewModel.navigator.showError(getErrorMessage(it))
//                })
//    }

    companion object {
        private val TAG = AppRepository::class.java.simpleName
        private var appRepository: AppRepository? = null

        fun instance(): AppRepository {
            if (appRepository == null) {
                synchronized(AppRepository) {
                    appRepository = AppRepository()
                }
            }
            return appRepository!!
        }
    }
}