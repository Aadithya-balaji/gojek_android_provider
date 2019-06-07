package com.gox.base.repository

import com.gox.base.chatmessage.ChatMainViewModel
import com.gox.base.data.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BaseModuleRepository : BaseRepository() {

    private val serviceId: String
        get() = Constants.BaseUrl.APP_BASE_URL

    fun sendMessage(viewModel: ChatMainViewModel, s: String, hashMap: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, BaseWebService::class.java)
                .sendMessage(s, hashMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ viewModel.successResponse.value = it },
                        { viewModel.errorResponse.value = getErrorMessage(it) })
    }

    fun getMessage(viewModel: ChatMainViewModel, token: String, adminService: String, id: Int): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.TAXI_BASE_URL, BaseWebService::class.java)
                .getMessages(token, adminService, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ viewModel.getMessageResponse.value = it },
                        { viewModel.errorResponse.value = getErrorMessage(it) })
    }

    companion object {
        private var appRepository: BaseModuleRepository? = null
        fun instance(): BaseModuleRepository {
            if (appRepository == null) synchronized(BaseModuleRepository) {
                appRepository = BaseModuleRepository()
            }
            return appRepository!!
        }
    }
}