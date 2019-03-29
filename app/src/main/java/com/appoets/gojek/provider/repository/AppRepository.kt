package com.appoets.gojek.provider.repository

import android.annotation.SuppressLint
import com.appoets.base.repository.BaseRepository
import com.appoets.base.utils.Logger
import com.appoets.gojek.provider.network.AppWebService
import com.appoets.gojek.provider.views.signin.SignInViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

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
                    Logger.e(TAG, it.message!!)
                    viewModel.navigator.showError(it.message!!)
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