package com.gox.foodservice.repositary

import com.gox.base.data.Constants
import com.gox.base.repository.BaseRepository
import com.gox.foodservice.ui.dashboard.FoodLiveTaskServiceViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FoodieRepository : BaseRepository() {

    private val serviceId: String
        get() = Constants.BaseUrl.APP_BASE_URL

    fun foodieCheckRequest(viewModel: FoodLiveTaskServiceViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, FoodieWebService::class.java)
                .foodieCheckRequest(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.foodieCheckRequestModel.postValue(it)
                    viewModel.showLoading.value = false
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                    viewModel.showLoading.value = false
                })
    }

    fun foodieUpdateRequest(viewModel: FoodLiveTaskServiceViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, FoodieWebService::class.java)
                .foodieUpdateRequest(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.foodieCheckRequestModel.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }


    fun foodieRatingRequest(viewModel: FoodLiveTaskServiceViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, FoodieWebService::class.java)
                .foodieRatingRequest(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.foodieRatingRequestModel.postValue(it)
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
