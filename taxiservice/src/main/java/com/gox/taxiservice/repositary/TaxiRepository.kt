package com.gox.taxiservice.repositary

import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import com.gox.base.repository.BaseRepository
import com.gox.taxiservice.model.DroppedStatusModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TaxiRepository : BaseRepository() {

    private val serviceId: String
        get() = Constants.BaseUrl.APP_BASE_URL

    fun checkRequest(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiCheckRequestAPI()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun taxiStatusUpdate(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiStatusUpdate(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun updateRequest(listener: ApiListener, model: DroppedStatusModel): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiDroppingStatus(model)
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun taxiDroppingStatus(listener: ApiListener, model: DroppedStatusModel): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiDroppingStatus(model)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun waitingTime(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .waitingTime(params)
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun confirmPayment(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .confirmPayment(params)
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun submitRating(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .submitRating(params)
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun taxiGetReason(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .taxiGetReason()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun taxiCancelReason(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, TaxiWebService::class.java)
                .cancelRequest(params)
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
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