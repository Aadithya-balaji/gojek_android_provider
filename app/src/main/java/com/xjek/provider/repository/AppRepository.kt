package com.xjek.provider.repository

import android.annotation.SuppressLint
import com.xjek.base.repository.BaseRepository
import com.xjek.provider.network.AppWebService
import com.xjek.provider.utils.Constant
import com.xjek.provider.views.change_password.ChangePasswordViewModel
import com.xjek.provider.views.forgot_password.ForgotPasswordViewModel
import com.xjek.provider.views.invitereferals.InviteReferalsViewModel
import com.xjek.provider.views.profile.ProfileViewModel
import com.xjek.provider.views.reset_password.ResetPasswordViewModel
import com.xjek.provider.views.sign_in.SignInViewModel
import com.xjek.provider.views.signup.SignupViewModel
import com.xjek.provider.views.splash.SplashViewModel
import com.xjek.provider.views.wallet.WalletViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.collections.HashMap

class AppRepository : BaseRepository() {

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
        return BaseRepository().createApiClient(Constant.baseUrl, AppWebService::class.java)
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
        return BaseRepository().createApiClient(Constant.baseUrl, AppWebService::class.java)
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
        return BaseRepository().createApiClient(Constant.baseUrl, AppWebService::class.java)
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
        return BaseRepository().createApiClient(Constant.baseUrl, AppWebService::class.java)
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
        return BaseRepository().createApiClient(Constant.baseUrl, AppWebService::class.java)
                .getCountries(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getCountryLiveData().postValue(it)
                }, {
                    Logger.getLogger(com.xjek.provider.repository.AppRepository.TAG).log(Level.SEVERE, it.message)
                    viewModel.gotoSignin()
                })
    }



    @SuppressLint("CheckResult")
    fun postSignup(viewModel: SignupViewModel, params: HashMap<String, RequestBody>, @Part filename: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(Constant.baseUrl, AppWebService::class.java)
                .postSignup(params, filename)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getSignupLiveData().postValue(it) }, {

                    viewModel.signupNavigator.showError(getErrorMessage(it))
                })
    }

    fun postChangePassword(viewModel: ChangePasswordViewModel, token: String,
                           params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(Constant.baseUrl, AppWebService::class.java)
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

    fun ValidateUser(viewModel: SignupViewModel,params: HashMap<String, String>):Disposable{
        return BaseRepository().createApiClient(Constant.baseUrl,AppWebService::class.java)
                .verifyUser(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(it.statusCode=="200"){

                    }
                },{
                    viewModel.navigator.showError(getErrorMessage(it))
                })

    }

    fun getProfile(viewModel:ProfileViewModel,token:String):Disposable{
        return BaseRepository().createApiClient(Constant.baseUrl,AppWebService::class.java)
                .getProfile(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                     if(it.statusCode=="200"){
                         viewModel.getProfileRespose().postValue(it)
                     }
                },{
                     viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })
    }

    fun getReferal(viewModel:InviteReferalsViewModel, token: String):Disposable{
        return  BaseRepository().createApiClient(Constant.baseUrl,AppWebService::class.java)
                .getProfile(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                     viewModel.getProfileLiveData().postValue(it)
                },{
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun updateProfile(viewModel:ProfileViewModel,param: HashMap<String,RequestBody>,@Part filename: MultipartBody.Part?):Disposable{
        return  BaseRepository().createApiClient(Constant.baseUrl,AppWebService::class.java)
                .updateProfile(param,filename)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.updateProfileResposne.postValue(it)
                },{
                    viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })
    }


    fun getCardList(viewModel:WalletViewModel,token:String):Disposable{
        return BaseRepository().createApiClient(Constant.baseUrl,AppWebService::class.java)
                .getCardList(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                           viewModel.cardResponseData.postValue(it)
                },{

                })
    }

    fun addWalletAmount(viewModel:WalletViewModel,params:HashMap<String,String>,token:String):Disposable{
        return BaseRepository().createApiClient(Constant.baseUrl,AppWebService::class.java)
                .addWalletMoney(token,params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                },{

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