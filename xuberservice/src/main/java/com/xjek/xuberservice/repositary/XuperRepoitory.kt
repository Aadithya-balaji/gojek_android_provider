package com.xjek.xuberservice.repositary

import com.xjek.base.data.Constants
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.base.repository.BaseRepository
import com.xjek.taximodule.ui.fragment.reason.ReasonViewModel
import com.xjek.xuberservice.invoice.XuperInvoiceViewModel
import com.xjek.xuberservice.rating.XuperRatingViewModel
import com.xjek.xuberservice.xuberMainActivity.XuberMainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part


class XuperRepoitory : BaseRepository() {
     private val serviceId: String
        get() = Constants.BaseUrl.APP_BASE_URL


    fun xuperCheckRequesst(viewModel: XuberMainViewModel, token: String, lat: String, lon: String): Disposable {
        return BaseRepository().createApiClient(serviceId, XuperApiService::class.java)
                .xuperCheckRequest(token, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.xuperCheckRequest.postValue(it)
                }, {
                     viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })

    }


    fun xuperUpdateRequest(viewModel: XuberMainViewModel, token: String, params: HashMap<String, RequestBody>,  image: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, XuperApiService::class.java)
                .xuperUpdateServcie(token, params, image)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.xuperUdpateRequest.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })

    }

    fun confirmPayment(viewModel: XuperInvoiceViewModel, token: String, params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, XuperApiService::class.java)
                .xuperUpdateServcie(token, params, image!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.invoiceLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })

    }
    fun xuperGetReason(viewModel: ReasonViewModel, token: String, type: String): Disposable {
        return BaseRepository().createApiClient(serviceId, XuperApiService::class.java)
                .getReasons(token, type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.mReasonResponseData.postValue(it)
                }, {
                    // viewModel.navigator.
                })
    }

    fun xuperCancelRequest(viewModel: XuberMainViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, XuperApiService::class.java)
                .cancelRequest(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.xuperCancelRequest.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }


    fun  xuperRatingUser(viewModel:XuperRatingViewModel,token:String,params: HashMap<String, String>):Disposable{
        return  BaseRepository().createApiClient(serviceId,XuperApiService::class.java)
                .xuperRating(token,params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.ratingLiveData.postValue(it)
                },{
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    companion object {
        private val TAG = XuperRepoitory::class.java.simpleName
        private var xuperRepoitory: XuperRepoitory? = null

        fun instance(): XuperRepoitory {
            if (xuperRepoitory == null) {
                synchronized(XuperRepoitory) {
                    xuperRepoitory = XuperRepoitory()
                }
            }
            return xuperRepoitory!!
        }
    }

}
