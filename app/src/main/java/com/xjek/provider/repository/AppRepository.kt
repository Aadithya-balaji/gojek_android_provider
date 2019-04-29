package com.xjek.provider.repository

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.base.repository.BaseRepository
import com.xjek.provider.network.AppWebService
import com.xjek.provider.views.change_password.ChangePasswordViewModel
import com.xjek.provider.views.dashboard.DashBoardViewModel
import com.xjek.provider.views.forgot_password.ForgotPasswordViewModel
import com.xjek.provider.views.home.HomeViewModel
import com.xjek.provider.views.incoming_request_taxi.IncomingRequestViewModel
import com.xjek.provider.views.invitereferals.InviteReferalsViewModel
import com.xjek.provider.views.notification.NotificationViewModel
import com.xjek.provider.views.profile.ProfileViewModel
import com.xjek.provider.views.reset_password.ResetPasswordViewModel
import com.xjek.provider.views.sign_in.SignInViewModel
import com.xjek.provider.views.signup.SignupViewModel
import com.xjek.provider.views.splash.SplashViewModel
import com.xjek.provider.views.transaction.TransactionViewModel
import com.xjek.provider.views.wallet.WalletViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap

class AppRepository : BaseRepository() {

    private val serviceId: String
        get() = PreferencesHelper.get(PreferencesKey.BASE_ID)

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
    fun getCountryList(viewModel: ViewModel, params: HashMap<String, Any?>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getCountries(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (viewModel is SignupViewModel)
                        viewModel.getCountryLiveData().postValue(it)
                    else if (viewModel is ProfileViewModel)
                        viewModel.countryListResponse.postValue(it)
                }, {
                    if (viewModel is SignupViewModel)
                        viewModel.gotoSignin()
                    else if (viewModel is ProfileViewModel)
                        viewModel.errorResponse.value = getErrorMessage(it)
                })
    }


    @SuppressLint("CheckResult")
    fun postSignup(viewModel: SignupViewModel, params: HashMap<String, RequestBody>,
                   @Part filename: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postSignUp(params, filename)
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

    /*fun getProfile(viewModel: ProfileViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
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
    }*/

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


    fun getProviderProfile(viewModel: ViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getProfile(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (viewModel is ProfileViewModel) {
                        viewModel.mProfileResponse.value = it
                        viewModel.loadingProgress.value = false
                    } /*else if (viewModel is InviteReferalsViewModel) {
                        viewModel.mProfileResponse.value = it
                        viewModel.loadingProgress.value = false
                    }*/
                }, {
                    if (viewModel is ProfileViewModel) {
                        viewModel.errorResponse.value = getErrorMessage(it)
                        viewModel.loadingProgress.value = false
                    } /*else if (viewModel is InviteReferalsViewModel) {
                        viewModel.errorResponse.value = getErrorMessage(it)
                        viewModel.loadingProgress.value = false
                    }*/
                })
    }


    fun profileUpdate(viewModel: ProfileViewModel, token: String, @PartMap params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .profileUpdate(token, params, image!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.mProfileUpdateResponse.value = it
                    viewModel.loadingProgress.value = false
                }, {
                    viewModel.errorResponse.value = getErrorMessage(it)
                    viewModel.loadingProgress.value = false
                })
    }


    fun getCardList(viewModel: WalletViewModel, token: String, limit: String, offset: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getCardList(token, limit, offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.cardResponseData.postValue(it)
                }, {

                })
    }

    fun addWalletAmount(viewModel: WalletViewModel, params: HashMap<String, String>, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .addWalletMoney(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.walletLiveResponse.postValue(it)
                }, {
                    viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })
    }

    fun getTransaction(viewModel: TransactionViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getWalletTransction(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.transcationLiveResponse.postValue(it)
                }, {

                })
    }

    fun deleteCDard(viewModel: WalletViewModel, token: String, cardId: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .deleteCard(token, cardId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.deleCardLivResponse.postValue(it)
                }, {
                    viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })

    }

    fun addCard(viewModel: WalletViewModel, params: HashMap<String, String>, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .addCard(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.addCardLiveResposne.postValue(it)
                }, {
                    viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })
    }

    fun changeAvilability(viewModel: HomeViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .changeAvailability(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                }, {

                })
    }

    fun checkRequest(viewModel: DashBoardViewModel, token: String, lat: String, lon: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getRequest(token, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.checkRequestLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    fun acceptIncomingRequest(viewModel: IncomingRequestViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .acceptIncomingRequest(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.acceptRequestLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrormessage(getErrorMessage(it))
                })
    }

    fun rejectIncomingRequest(viewModel: IncomingRequestViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .rejectIncomingRequest(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.rejectRequestLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrormessage(getErrorMessage(it))
                })
    }

    fun getNotification(viewModel: NotificationViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getNotification(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.notificationResponse.value = it
                }, {
                    viewModel.loadingProgress.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }


    fun changeOnlineStatus(viewModel: HomeViewModel,token:String, status: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .changeOnlineStatus(token,status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.onlineStatusLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

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