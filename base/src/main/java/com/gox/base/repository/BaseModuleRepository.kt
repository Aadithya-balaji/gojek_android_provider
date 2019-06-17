package com.gox.base.repository

import com.gox.base.data.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BaseModuleRepository : BaseRepository() {

    fun sendMessage(listener: ApiListener, hashMap: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, BaseWebService::class.java)
                .sendMessage(hashMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getMessage(listener: ApiListener, adminService: String, id: Int): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.TAXI_BASE_URL, BaseWebService::class.java)
                .getMessages(adminService, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    companion object {
        private var mRepository: BaseModuleRepository? = null
        fun instance(): BaseModuleRepository {
            if (mRepository == null) synchronized(BaseModuleRepository) {
                mRepository = BaseModuleRepository()
            }
            return mRepository!!
        }
    }
}