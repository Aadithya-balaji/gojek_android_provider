package com.xjek.foodservice.view.repositary

import com.xjek.base.data.Constants
import com.xjek.base.repository.BaseRepository
import com.xjek.foodservice.view.FoodLiveTaskServiceViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FoodieRepository : BaseRepository() {

    private val serviceId: String
        get() = Constants.BaseUrl.APP_BASE_URL

    fun foodieCheckRequest(viewModel: FoodLiveTaskServiceViewModel, token: String, lat: String, lon: String): Disposable {
        return BaseRepository().createApiClient(serviceId, FoodieWebService::class.java)
                .foodieCheckRequest(token, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.foodieCheckRequestModel.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    companion object {
        private var foodieRepository: FoodieRepository? = null
        fun instance(): FoodieRepository {
            if (foodieRepository == null) {
                synchronized(FoodieRepository) {
                    foodieRepository = FoodieRepository()
                }
            }
            return foodieRepository!!
        }
    }
}
