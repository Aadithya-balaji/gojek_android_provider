package com.xjek.provider.repository

import android.annotation.SuppressLint
import com.xjek.base.repository.BaseRepository
import com.xjek.provider.views.change_password.ChangePasswordViewModel
import com.xjek.provider.network.AppWebService
import com.xjek.provider.views.signin.SignInViewModel
import com.xjek.provider.views.signup.SignupViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class AppRepository : BaseRepository() {

    @SuppressLint("CheckResult")
    fun postLogin(viewModel: SignInViewModel, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(AppWebService::class.java)
                .postLogin(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode.equals("200"))
                        viewModel.getLoginObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(it.message!!)
                })
    }


    fun postChangePassword(viewModel: ChangePasswordViewModel, token: String,
                           params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(AppWebService::class.java)
                .postChangePassword(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode.equals("200"))
                        viewModel.getChangePasswordObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(it.message!!)
                })
    }

    @SuppressLint("CheckResult")
    fun postSignup(viewModel: SignupViewModel, params: HashMap<String, String>):Disposable{
        return  BaseRepository().createApiClient(AppWebService::class.java)
                .postSignup(params )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({viewModel.getSignupLiveData().postValue(it)},{
                    viewModel.signupNavigator.showError(it.message!!)
                })
    }

    @SuppressLint("CheckResult")
    fun getCountryList(viewModel: SignupViewModel,countryID:String):Disposable {
        return BaseRepository().createApiClient(AppWebService::class.java)
                .getCountries(countryID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getCountryLiveData().postValue(it)
                },{
                    Logger.getLogger(TAG).log(Level.SEVERE,it.message)
                    viewModel.gotoSignin()
                })
    }

    @SuppressLint("CheckResult")
    fun getStateList(viewModel: SignupViewModel,stateID:String):Disposable {
        return BaseRepository().createApiClient(AppWebService::class.java)
                .getStatelist(stateID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getStateLiveData().postValue(it)
                })
    }

    @SuppressLint("CheckResult")
    fun getCityList(viewModel: SignupViewModel,cityID:String):Disposable {
      return  BaseRepository().createApiClient(AppWebService::class.java)
                .getCityList(cityID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getCityLiveData().postValue(it)
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