package com.gox.xuberservice.repositary

import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import com.gox.base.repository.BaseRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class XUberRepository : BaseRepository() {

    private val serviceId: String
        get() = Constants.BaseUrl.APP_BASE_URL

    fun xUberCheckRequest(listener: ApiListener, lat: String, lon: String): Disposable {
        return BaseRepository().createApiClient(serviceId, XUberApiService::class.java)
                .xUberCheckRequest(lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun xUberUpdateRequest(listener: ApiListener,
                           params: HashMap<String, RequestBody>,
                           @Part frontImage: MultipartBody.Part?,
                           @Part backImage: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, XUberApiService::class.java)
                .xUberUpdateServices(params, frontImage, backImage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun confirmPayment(listener: ApiListener, params: HashMap<String, RequestBody>): Disposable {
        return BaseRepository().createApiClient(serviceId, XUberApiService::class.java)
                .confirmPayment(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun xUberGetReason(listener: ApiListener, type: String): Disposable {
        return BaseRepository().createApiClient(serviceId, XUberApiService::class.java)
                .getReasons(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun xUberCancelRequest(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, XUberApiService::class.java)
                .cancelRequest(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun xUberRatingUser(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, XUberApiService::class.java)
                .xUberRating(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    companion object {
        private var mRepository: XUberRepository? = null
        fun instance(): XUberRepository {
            if (mRepository == null) synchronized(XUberRepository) {
                mRepository = XUberRepository()
            }
            return mRepository!!
        }
    }
}