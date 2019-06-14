package com.gox.foodservice.repositary

import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import com.gox.base.repository.BaseRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FoodieRepository : BaseRepository() {

    private val serviceId: String get() = Constants.BaseUrl.APP_BASE_URL

    fun foodieCheckRequest(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, FoodieWebService::class.java)
                .foodieCheckRequest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun foodieUpdateRequest(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, FoodieWebService::class.java)
                .foodieUpdateRequest(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun foodieRatingRequest(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, FoodieWebService::class.java)
                .foodieRatingRequest(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    companion object {
        private var foodieRepository: FoodieRepository? = null
        fun FoodieRepoInstance(): FoodieRepository {
            if (foodieRepository == null) synchronized(FoodieRepository) {
                foodieRepository = FoodieRepository()
            }
            return foodieRepository!!
        }
    }
}
