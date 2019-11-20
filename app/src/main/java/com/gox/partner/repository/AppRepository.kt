package com.gox.partner.repository

import com.gox.app.ui.verifyotp.VerifyOTPViewModel
import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import com.gox.base.repository.BaseRepository
import com.gox.partner.network.AppWebService
import com.gox.partner.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap

class AppRepository : BaseRepository() {

    private val serviceId: String
        get() = Constants.BaseUrl.APP_BASE_URL

    fun getConfig(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(AppWebService::class.java)
                .getConfig(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        listener.success(it)
                }, { listener.fail(it) })
    }

    fun postLogin(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postLogin(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        listener.success(it)
                }, { listener.fail(it) })
    }

    fun sendOTP(listener: ApiListener, @PartMap params: HashMap<String, RequestBody>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .sendOTP(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    listener.success(it)
                }, {
                    listener.fail(it)
                })
    }

    fun verifyOTP(viewModel: VerifyOTPViewModel, @PartMap params: HashMap<String, RequestBody>): Disposable{
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .verifyOTP(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.verifyOTPResponse.value = it
                }, {
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun postSocialLogin(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postSocialLogin(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        listener.success(it)
                }, { listener.fail(it) })
    }

    fun postForgotPassword(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postForgotPassword(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200") listener.success(it)
                }, { listener.fail(it) })
    }

    fun postResetPassword(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postResetPassword(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ if (it.statusCode == "200") listener.success(it) }, { listener.fail(it) })
    }

    fun getCountryList(listener: ApiListener, params: HashMap<String, Any?>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getCountries(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun postSignUp(listener: ApiListener, params: HashMap<String, RequestBody>, @Part filename: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postSignUp(params, filename)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }



    fun postChangePassword(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postChangePassword(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200") listener.success(it)
                }, { listener.fail(it) })
    }

    fun validateUser(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .verifyUser(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({}, { listener.fail(it) })

    }

    fun getReferral(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }


    fun getProfile(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getProviderProfile(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getProfileDetails(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }


    fun profileUpdate(listener: ApiListener, @PartMap params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .profileUpdate(params, image)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }


    fun getCardList(listener: ApiListener, limit: String, offset: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getCardList(limit, offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun addWalletAmount(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .addWalletMoney(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getTransaction(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getWalletTransction()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun deleteCard(listener: ApiListener, cardId: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .deleteCard(cardId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun addCard(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .addCard(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun checkRequest(listener: ApiListener, lat: String, lon: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getRequest(lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun acceptIncomingRequest(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .acceptIncomingRequest(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun rejectIncomingRequest(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .rejectIncomingRequest(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getNotification(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getNotification()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }


    fun changeOnlineStatus(listener: ApiListener, status: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .changeOnlineStatus(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getServices(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getServices()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ if (it.statusCode == "200") listener.success(it) }, { listener.fail(it) })
    }

    fun getServiceCategories(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getServiceCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getSubServiceCategories(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getSubServiceCategories(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getSubServicePriceCategories(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getSubServicePriceCategories(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getRides(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getRides()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getShops(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getShops()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun postVehicle(listener: ApiListener,
                    params: HashMap<String, RequestBody>,
                    vehicleMultiPart: MultipartBody.Part?,
                    rcBookMultiPart: MultipartBody.Part?,
                    insuranceMultipart: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postVehicle(params, vehicleMultiPart, rcBookMultiPart, insuranceMultipart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ if (it.statusCode == "200") listener.success(it) }, { listener.fail(it) })
    }

    fun editVehicle(listener: ApiListener,
                    params: HashMap<String, RequestBody>,
                    vehicleMultiPart: MultipartBody.Part?,
                    rcBookMultiPart: MultipartBody.Part?,
                    insuranceMultipart: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .editVehicle(params, vehicleMultiPart, rcBookMultiPart, insuranceMultipart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ if (it.statusCode == "200") listener.success(it) }, { listener.fail(it) })
    }

    fun postVehicle(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postVehicle(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ if (it.statusCode == "200") listener.success(it) }, { listener.fail(it) })
    }


    fun editVehicle(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .editVehicle(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ if (it.statusCode == "200") listener.success(it) }, { listener.fail(it) })
    }

    fun getBankTemplate(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getBankTemplate()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun postAddBankDetails(listener: ApiListener, body: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postAddBankDetails(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun postEditBankDetails(listener: ApiListener, body: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postEditBankDetails(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getDocumentList(listener: ApiListener, documentType: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getDocuments(documentType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun postDocument(listener: ApiListener,
                     @PartMap params: HashMap<String, RequestBody>,
                     @Part frontImage: MultipartBody.Part?,
                     @Part backImage: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postDocument(params, frontImage, backImage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getTransportCurrentHistory(listener: ApiListener, hashMap: HashMap<String, String>, selectService: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getTransportHistory(selectService, hashMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getPastOrderHistory(listener: ApiListener, params: HashMap<String, String>, selectService: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getPastHistory(selectService, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getTransPortDetail(listener: ApiListener, selected_id: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getHistoryDetail(selected_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getServiceDetail(listener: ApiListener, selectedID: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getServiceDetail(selectedID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getOrderDetail(listener: ApiListener, selectedID: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getOrderDetail(selectedID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getDisputeList(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getDisputeList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getDisputeList(listener: ApiListener, serviceType: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getDisputeReasons(serviceType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getEarnings(listener: ApiListener, userId: Int): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getEarnings(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun addTaxiDispute(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .postTaxiDispute(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun addServiceDispute(listener: ApiListener,  params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .postServiceDispute(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun addOrderDispute(listener: ApiListener, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .postOrderDispute(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getTransportDisputeStatus(listener: ApiListener, requestID: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getTransportDisputeStatus(requestID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }


    fun getOrderDisputeStatus(listener: ApiListener, requestID: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getOrderDisputeStatus(requestID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun getServiceDisputeStatus(listener: ApiListener,requestID: String):Disposable {
        return  BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL,AppWebService::class.java)
                .getServiceDisputeStatus(requestID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({listener.success(it)},{listener.fail(it)})
    }

    fun logoutApp(listener: ApiListener): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.TAXI_BASE_URL, AppWebService::class.java)
                .logout()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({listener.success(it)}, {listener.fail(it)})
    }

    companion object {
        private var mRepository: AppRepository? = null
        fun instance(): AppRepository {
            if (mRepository == null) synchronized(AppRepository) {
                mRepository = AppRepository()
            }
            return mRepository!!
        }
    }
}