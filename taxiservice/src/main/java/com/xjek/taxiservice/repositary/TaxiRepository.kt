package com.xjek.taxiservice.repositary

import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.base.repository.BaseRepository
import com.xjek.taxiservice.views.main.ActivityTaxiModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TaxiRepository : BaseRepository() {

    private val serviceId: String
        get() = PreferencesHelper.get(PreferencesKey.BASE_ID)

    fun checkRequest(viewModel: ActivityTaxiModule, token: String, lat: String, lon: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .taxiCheckRequestAPI(token, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.checkStatusTaxiLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    fun waitingTime(viewModel: ActivityTaxiModule, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .waitingTime(token, params)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.waitingTimeLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    companion object {
        private val TAG = TaxiRepository::class.java.simpleName
        private var taxiRepository: TaxiRepository? = null

        fun instance(): TaxiRepository {
            if (taxiRepository == null) {
                synchronized(TaxiRepository) {
                    taxiRepository = TaxiRepository()
                }
            }
            return taxiRepository!!
        }
    }


}