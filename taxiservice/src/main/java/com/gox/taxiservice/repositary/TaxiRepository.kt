package com.gox.taxiservice.repositary

import com.gox.base.data.Constants
import com.gox.base.repository.BaseRepository
import com.gox.taxiservice.model.DroppedStatusModel
import com.gox.taxiservice.views.invoice.TaxiInvoiceViewModel
import com.gox.taxiservice.views.main.TaxiDashboardViewModel
import com.gox.taxiservice.views.rating.TaxiRatingViewModel
import com.gox.taxiservice.views.reasons.TaxiCancelReasonViewModel
import com.gox.taxiservice.views.tollcharge.TollChargeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TaxiRepository : BaseRepository() {

    private val serviceId: String
        get() = Constants.BaseUrl.APP_BASE_URL

    fun checkRequest(viewModel: TaxiDashboardViewModel, token: String, lat: Double, lon: Double): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
//                .taxiCheckRequestAPI(token, lat, lon)
                .taxiCheckRequestAPI(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    try {
                        viewModel.checkStatusTaxiLiveData.postValue(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, {
                    try {
                        viewModel.navigator.showErrorMessage(getErrorMessage(it))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
    }

    fun taxiStatusUpdate(viewModel: TaxiDashboardViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiStatusUpdate(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    try {
                        viewModel.callTaxiCheckStatusAPI()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, {
                    try {
                        viewModel.navigator.showErrorMessage(getErrorMessage(it))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
    }

    fun updateRequest(viewModel: TollChargeViewModel, token: String, model: DroppedStatusModel): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiDroppingStatus(token, model)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.mLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    fun taxiDroppingStatus(viewModel: TaxiDashboardViewModel, token: String, model: DroppedStatusModel): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiDroppingStatus(token, model)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    try {
                        viewModel.callTaxiCheckStatusAPI()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, {
                    try {
                        viewModel.navigator.showErrorMessage(getErrorMessage(it))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
    }

    fun waitingTime(viewModel: TaxiDashboardViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .waitingTime(token, params)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.waitingTimeLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    fun confirmPayment(viewModel: TaxiInvoiceViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .confirmPayment(token, params)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.paymentLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    fun submitRating(viewModel: TaxiRatingViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .submitRating(token, params)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.ratingLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    fun taxiGetReason(viewModel: TaxiCancelReasonViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiGetReason(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.mResponse.postValue(it)
                }, {
                    // viewModelXUberCancel.navigator.
                })
    }

    fun taxiCancelReason(viewModel: TaxiDashboardViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .cancelRequest(token, params)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.taxiCancelRequest.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    companion object {
        private var taxiRepository: TaxiRepository? = null

        fun instance(): TaxiRepository {
            if (taxiRepository == null) synchronized(TaxiRepository) {
                taxiRepository = TaxiRepository()
            }
            return taxiRepository!!
        }
    }
}