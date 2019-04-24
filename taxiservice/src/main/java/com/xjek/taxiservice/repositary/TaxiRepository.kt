package com.xjek.taxiservice.repositary

import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.base.repository.BaseRepository
import com.xjek.taxiservice.views.invoice.InvoiceModule
import com.xjek.taxiservice.views.main.ActivityTaxiModule
import com.xjek.taxiservice.views.tollcharge.TollChargeViewModel
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

    fun taxiStatusUpdate(viewModel: ActivityTaxiModule, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
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


    fun updateRequest(viewModel: TollChargeViewModel,token:String,params:HashMap<String,String>):Disposable{
       return BaseRepository().createApiClient(serviceId,AppWebService::class.java)
                 .taxiStatusUpdate(token,params)
               .subscribeOn(Schedulers.io())
               .subscribe({
                     viewModel.updateRequestLiveData.postValue(it)
                 }, {
                     viewModel.navigator.showErrorMessage(getErrorMessage(it))
                 })
    }

    fun confirmPayment(viewModel:InvoiceModule,token:String,params: HashMap<String, String>):Disposable{
        return BaseRepository().createApiClient(serviceId,AppWebService::class.java)
                .ConfirmPayment(token,params)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.paymentLiveData.postValue(it)
                },{
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