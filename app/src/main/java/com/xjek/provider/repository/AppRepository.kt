package com.xjek.provider.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.xjek.base.data.Constants
import com.xjek.base.repository.BaseRepository
import com.xjek.provider.network.AppWebService
import com.xjek.provider.views.add_edit_document.AddEditDocumentViewModel
import com.xjek.provider.views.add_vehicle.AddVehicleViewModel
import com.xjek.provider.views.change_password.ChangePasswordViewModel
import com.xjek.provider.views.currentorder_fragment.CurrentOrderViewModel
import com.xjek.provider.views.dashboard.DashBoardViewModel
import com.xjek.provider.views.earnings.EarningsViewModel
import com.xjek.provider.views.forgot_password.ForgotPasswordViewModel
import com.xjek.provider.views.history_details.HistoryDetailViewModel
import com.xjek.provider.views.home.HomeViewModel
import com.xjek.provider.views.incoming_request_taxi.IncomingRequestViewModel
import com.xjek.provider.views.invitereferals.InviteReferalsViewModel
import com.xjek.provider.views.manage_bank_details.ManageBankDetailsViewModel
import com.xjek.provider.views.manage_services.ManageServicesViewModel
import com.xjek.provider.views.notification.NotificationViewModel
import com.xjek.provider.views.profile.ProfileViewModel
import com.xjek.provider.views.reset_password.ResetPasswordViewModel
import com.xjek.provider.views.set_service.SetServiceViewModel
import com.xjek.provider.views.set_service_category_price.SetServicePriceViewModel
import com.xjek.provider.views.set_subservice.SetSubServiceViewModel
import com.xjek.provider.views.setup_vehicle.SetupVehicleViewModel
import com.xjek.provider.views.sign_in.SignInViewModel
import com.xjek.provider.views.signup.SignupViewModel
import com.xjek.provider.views.splash.SplashViewModel
import com.xjek.provider.views.transaction.TransactionViewModel
import com.xjek.provider.views.wallet.WalletViewModel
import com.xjek.xjek.ui.pastorder_fragment.PastOrderViewModel
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

    @SuppressLint("CheckResult")
    fun getConfig(viewModel: SplashViewModel, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(AppWebService::class.java)
                .getConfig(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getConfigObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    @SuppressLint("CheckResult")
    fun postLogin(viewModel: SignInViewModel, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postLogin(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getLoginObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    @SuppressLint("CheckResult")
    fun postSocialLogin(viewModel: SignInViewModel, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postSocialLogin(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getLoginObservable().postValue(it)
                }, {
                    viewModel.navigator.showAlert(getErrorMessage(it))
                })
    }

    @SuppressLint("CheckResult")
    fun postForgotPassword(viewModel: ForgotPasswordViewModel, params: HashMap<String, String>):
            Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postForgotPassword(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getForgotPasswordObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    @SuppressLint("CheckResult")
    fun postResetPassword(viewModel: ResetPasswordViewModel, params: HashMap<String, String>):
            Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postResetPassword(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getResetPasswordObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    @SuppressLint("CheckResult")
    fun getCountryList(viewModel: ViewModel, params: HashMap<String, Any?>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getCountries(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (viewModel is SignupViewModel)
                        viewModel.getCountryLiveData().postValue(it)
                    else if (viewModel is ProfileViewModel)
                        viewModel.countryListResponse.postValue(it)
                }, {
                    if (viewModel is SignupViewModel)
                        viewModel.gotoSignin()
                    else if (viewModel is ProfileViewModel)
                        viewModel.errorResponse.value = getErrorMessage(it)
                })
    }


    @SuppressLint("CheckResult")
    fun postSignup(viewModel: SignupViewModel, params: HashMap<String, RequestBody>,
                   @Part filename: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postSignUp(params, filename)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getSignupLiveData().postValue(it)
                }, {

                    viewModel.signupNavigator.showError(getErrorMessage(it))
                })
    }

    fun postChangePassword(viewModel: ChangePasswordViewModel, token: String,
                           params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postChangePassword(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getChangePasswordObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun ValidateUser(viewModel: SignupViewModel, params: HashMap<String, String>): Disposable {

        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .verifyUser(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200") {

                    }
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })

    }

    /*fun getProfile(viewModel: ProfileViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getProfile(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200") {
                        viewModel.getProfileRespose().postValue(it)
                    }
                }, {
                    viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })
    }*/

    fun getReferal(viewModel: InviteReferalsViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getProfile(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.getProfileLiveData().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }


    fun getProviderProfile(viewModel: ViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getProfile(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (viewModel is ProfileViewModel) {
                        viewModel.mProfileResponse.value = it
                        viewModel.loadingProgress.value = false
                    } else if (viewModel is DashBoardViewModel) {
                        viewModel.mProfileResponse.value = it
                    }
                }, {
                    if (viewModel is ProfileViewModel) {
                        viewModel.errorResponse.value = getErrorMessage(it)
                        viewModel.loadingProgress.value = false
                    } /*else if (viewModel is InviteReferalsViewModel) {
                        viewModel.errorResponse.value = getErrorMessage(it)
                        viewModel.loadingProgress.value = false
                    }*/
                })
    }


    fun profileUpdate(viewModel: ProfileViewModel, token: String, @PartMap params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .profileUpdate(token, params, image!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.mProfileUpdateResponse.value = it
                    viewModel.loadingProgress.value = false
                }, {
                    viewModel.errorResponse.value = getErrorMessage(it)
                    viewModel.loadingProgress.value = false
                })
    }


    fun getCardList(viewModel: WalletViewModel, token: String, limit: String, offset: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getCardList(token, limit, offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.cardResponseData.postValue(it)
                }, {

                })
    }

    fun addWalletAmount(viewModel: WalletViewModel, params: HashMap<String, String>, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .addWalletMoney(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.walletLiveResponse.postValue(it)
                }, {
                    viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })
    }

    fun getTransaction(viewModel: TransactionViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getWalletTransction(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.transcationLiveResponse.postValue(it)
                }, {
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun deleteCDard(viewModel: WalletViewModel, token: String, cardId: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .deleteCard(token, cardId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.deleCardLivResponse.postValue(it)
                }, {
                    viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })

    }

    fun addCard(viewModel: WalletViewModel, params: HashMap<String, String>, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .addCard(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.addCardLiveResposne.postValue(it)
                }, {
                    viewModel.navigator.showErrorMsg(getErrorMessage(it))
                })
    }

    fun changeAvilability(viewModel: HomeViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .changeAvailability(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                }, {

                })
    }

    fun checkRequest(viewModel: DashBoardViewModel, token: String, lat: String, lon: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getRequest(token, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.checkRequestLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                })
    }

    fun acceptIncomingRequest(viewModel: IncomingRequestViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .acceptIncomingRequest(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.acceptRequestLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrormessage(getErrorMessage(it))
                })
    }

    fun rejectIncomingRequest(viewModel: IncomingRequestViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .rejectIncomingRequest(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.rejectRequestLiveData.postValue(it)
                }, {
                    viewModel.navigator.showErrormessage(getErrorMessage(it))
                })
    }

    fun getNotification(viewModel: NotificationViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getNotification(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.notificationResponse.value = it
                }, {
                    viewModel.loadingProgress.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }


    fun changeOnlineStatus(viewModel: HomeViewModel, token: String, status: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .changeOnlineStatus(token, status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.onlineStatusLiveData.postValue(it)
                    viewModel.showLoading.value = false
                }, {
                    viewModel.navigator.showErrorMessage(getErrorMessage(it))
                    viewModel.showLoading.value = false
                })
    }

    fun getServices(viewModel: ManageServicesViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getServices(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getServicesObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun getServiceCategories(viewModel: SetServiceViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getServiceCategories(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.serviceCategoriesResponse.postValue(it)
                }, {
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun getSubServiceCategories(viewModel: SetSubServiceViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getSubServiceCategories(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.subServiceCategoriesResponse.postValue(it)
                }, {
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun getSubServicePriceCategories(viewModel: SetServicePriceViewModel, token: String, params: HashMap<String, String>): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getSubServicePriceCategories(token, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.subServiceCategoriesPriceResponse.postValue(it)
                }, {
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun getRides(viewModel: SetupVehicleViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getRides(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getVehicleDataObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun getShops(viewModel: SetupVehicleViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getShops(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getVehicleDataObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun getVehicleCategories(viewModel: AddVehicleViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getVehicleCategories(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getVehicleCategoryObservable().postValue(it)
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }

    fun postVehicle(viewModel: AddVehicleViewModel, params: HashMap<String, RequestBody>, vehicleMultitpart: MultipartBody.Part?, rcBookMultipart: MultipartBody.Part?, insuranceMultipart: MultipartBody.Part?
    ): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postVehicle(params, vehicleMultitpart, rcBookMultipart, insuranceMultipart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingObservable.value = false
                    if (it.statusCode == "200")
                        viewModel.getVehicleResponseObservable().postValue(it)
                }, {
                    viewModel.loadingObservable.value = false
                    viewModel.navigator.showError(getErrorMessage(it))
                })
    }


    fun editVehicle(viewModel: AddVehicleViewModel, params: HashMap<String, RequestBody>, vehicleMultitpart: MultipartBody.Part?, rcBookMultipart: MultipartBody.Part?, insuranceMultipart: MultipartBody.Part?
    ): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .editVehicle(params, vehicleMultitpart, rcBookMultipart, insuranceMultipart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.statusCode == "200")
                        viewModel.getVehicleResponseObservable().postValue(it)
                    viewModel.loadingObservable.value = false
                }, {
                    viewModel.navigator.showError(getErrorMessage(it))
                    viewModel.loadingObservable.value = false
                })
    }

//    fun getServices(viewModel: SetupVehicleViewModel, token: String): Disposable {
//        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
//                .getRides(token)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe({
//                    if (it.statusCode == "200")
//                        viewModel.getVehicleDataObservable().postValue(it)
//                }, {
//                    viewModel.navigator.showError(getErrorMessage(it))
//                })
//    }


    fun getBankTemplate(viewModel: ManageBankDetailsViewModel, token: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getBankTemplate(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.showLoading.value = false
                    viewModel.bankResponse.postValue(it)
                }, {
                    viewModel.showLoading.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }


    fun postAddBankDetails(viewModel: ManageBankDetailsViewModel, token: String, body: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postAddBankDetails(token, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.addEditBankResponse.postValue(it)
                }, {
                    viewModel.showLoading.value = false
                    viewModel.addEditBankErrorResponse.value = getErrorMessage(it)
                })
    }

    fun postEditBankDetails(viewModel: ManageBankDetailsViewModel, token: String, body: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postEditBankDetails(token, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.addEditBankResponse.postValue(it)
                }, {
                    viewModel.showLoading.value = false
                    viewModel.addEditBankErrorResponse.value = getErrorMessage(it)
                })
    }

    fun getDocumentList(viewModel: AddEditDocumentViewModel, documentType: String): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .getDocuments(documentType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.showLoading.value = false
                    viewModel.documentResponse.postValue(it)
                }, {
                    viewModel.showLoading.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }


    fun postDocument(viewModel: AddEditDocumentViewModel, @PartMap params: HashMap<String, RequestBody>, @Part frontImage: MultipartBody.Part?, @Part backImage: MultipartBody.Part?): Disposable {
        return BaseRepository().createApiClient(serviceId, AppWebService::class.java)
                .postDocument(params, frontImage, backImage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.showLoading.value = false
                    viewModel.addDocumentResponse.postValue(it)
                }, {
                    viewModel.showLoading.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }


    fun getTransaportCurrentHistory(viewModel: CurrentOrderViewModel, token: String, hashMap: HashMap<String, String>
                                    , selectedservice: String): Disposable {

        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getTransportHistory(token, selectedservice, hashMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.transportCurrentHistoryResponse.value = it
                }, {
                    viewModel.loadingProgress.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun getPastORderHistory(viewModel: PastOrderViewModel, token: String, params: HashMap<String, String>
                            , selectedservice: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getPastHistory(token, selectedservice, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.transportHistoryResponse.value = it
                }, {
                    viewModel.loadingProgress.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun getServiceCurrentHistory(viewModel: CurrentOrderViewModel, token: String, hashMap: HashMap<String
            , String>): Disposable {

        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getServiceHistory(token, hashMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.transportCurrentHistoryResponse.value = it
                }, {
                    viewModel.loadingProgress.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }


    fun getHistoryDetail(viewModel: HistoryDetailViewModel, token: String, selected_id: String): Disposable {

        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getHistoryDetail(token, selected_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.historyDetailResponse.value = it
                }, {
                    viewModel.loadingProgress.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun getUpcomingHistoryDetail(viewModel: HistoryDetailViewModel, token: String, selectedID: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getUpcomingHistoryDetail(token, selectedID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.historyUpcomingDetailResponse.value = it
                }, {
                    viewModel.loadingProgress.value = false
                    Log.d("_D_ERROR", it.message)
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }


    fun getDisputeList(viewModel: HistoryDetailViewModel, token: String): Disposable {

        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getDisputeList(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.disputeListData.value = it
                }, {
                    viewModel.loadingProgress.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun addDispute(viewModel: HistoryDetailViewModel, token: String, hashMap: HashMap<String, String>): Disposable {

        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .addDispute(token, hashMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                }, {
                    viewModel.loadingProgress.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun getDisputeStatus(viewModel: HistoryDetailViewModel, token: String, selected_id: String): Disposable {
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getDisputeStatus(token, selected_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.disputeStatusResponse.value = it
                }, {
                    viewModel.loadingProgress.value = false
                    viewModel.errorResponse.value = getErrorMessage(it)
                })
    }

    fun getMonthlyEarnings(viewModel: EarningsViewModel, token: String, userId: Int): Disposable {
        viewModel.loadingProgress.value = true
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getMonthlyEarnings(token, userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.earningsMonth.value = it
                }, {
                    viewModel.loadingProgress.value = false
                })
    }

    fun getWeeklyEarnings(viewModel: EarningsViewModel, token: String, userId: Int): Disposable {
        viewModel.loadingProgress.value = true
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getWeeklyEarnings(token, userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.earningsWeek.value = it
                }, {
                    viewModel.loadingProgress.value = false

                })
    }

    fun getDailyEarnings(viewModel: EarningsViewModel, token: String, userId: Int): Disposable {
        viewModel.loadingProgress.value = true
        return BaseRepository().createApiClient(Constants.BaseUrl.APP_BASE_URL, AppWebService::class.java)
                .getDailyEarnings(token, userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    viewModel.loadingProgress.value = false
                    viewModel.earningsDay.value = it
                }, {
                    viewModel.loadingProgress.value = false
                })
    }

    companion object {
        private val TAG = AppRepository::class.java.simpleName
        private var appRepository: AppRepository? = null

        fun instance(): AppRepository {
            if (appRepository == null) {
                synchronized(AppRepository) {
                    appRepository = AppRepository()
                }
            }
            return appRepository!!
        }
    }
}