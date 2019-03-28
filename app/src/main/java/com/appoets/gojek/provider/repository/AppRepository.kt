package com.appoets.gojek.provider.repository

import android.annotation.SuppressLint
import com.appoets.base.repository.BaseRepository
import com.appoets.gojek.provider.network.AppWebService
import com.appoets.gojek.provider.views.signin.SignInViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class AppRepository : BaseRepository() {

    @SuppressLint("CheckResult")
    fun postLogin(viewModel: SignInViewModel, params: HashMap<String, String>) {
        BaseRepository().createApiClient(AppWebService::class.java)
                .postLogin(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getLoginObservable().postValue(it)
                }, {
                    Logger.getLogger(TAG).log(Level.SEVERE, it.message)
                    viewModel.signInListener.showError(it.message!!)
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