package com.xjek.taxiservice.repositary

import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.base.repository.BaseRepository
import com.xjek.taxiservice.views.invoice.TaxiInvoiceViewModel
import com.xjek.taxiservice.views.main.TaxiDashboardViewModel
import com.xjek.taxiservice.views.rating.TaxiRatingViewModel
import com.xjek.taxiservice.views.tollcharge.TollChargeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TaxiRepository : BaseRepository() {

    private val serviceId: String
        get() = PreferencesHelper.get(PreferencesKey.BASE_ID)

    fun checkRequest(dashboardViewModel: TaxiDashboardViewModel, token: String, lat: String, lon: String): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiCheckRequestAPI(token, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    try {
                        dashboardViewModel.checkStatusTaxiLiveData.postValue(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, {
                    try {
                        dashboardViewModel.navigator.showErrorMessage(getErrorMessage(it))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
    }

    fun taxiStatusUpdate(dashboardViewModel: TaxiDashboardViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiStatusUpdate(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    try {
                        dashboardViewModel.callTaxiCheckStatusAPI()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, {
                    try {
                        dashboardViewModel.navigator.showErrorMessage(getErrorMessage(it))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
    }

    fun waitingTime(dashboardViewModel: TaxiDashboardViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .waitingTime(token, params)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    dashboardViewModel.waitingTimeLiveData.postValue(it)
                }, {
                    dashboardViewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }


    fun updateRequest(viewModel: TollChargeViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiStatusUpdate(token, params)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.mLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    fun confirmPayment(viewModelTaxi: TaxiInvoiceViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .confirmPayment(token, params)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModelTaxi.paymentLiveData.postValue(it)
                }, {
                    viewModelTaxi.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    fun submitRating(viewModelTaxi: TaxiRatingViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .submitRating(token, params)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModelTaxi.ratingLiveData.postValue(it)
                }, {
                    viewModelTaxi.navigator.showErrorMessage(getErrorMessage(it))
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