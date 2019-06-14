package com.gox.partner.views.history_details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.partner.models.*
import com.gox.partner.repository.AppRepository

class HistoryDetailViewModel : BaseViewModel<CurrentOrderDetailsNavigator>() {

    private val appRepository = AppRepository.instance()
    private val preferenceHelper = PreferencesHelper

    var historyDetailResponse = MutableLiveData<HistoryDetailModel>()
    var historyUpcomingDetailResponse = MutableLiveData<HistoryDetailModel>()
    var transportDetail = MutableLiveData<HistoryDetailModel.ResponseData.Transport>()
    var orderDetail = MutableLiveData<HistoryDetailModel.ResponseData.Order>()
    var serviceDetail = MutableLiveData<HistoryDetailModel.ResponseData.Service>()
    var disputeListData = MutableLiveData<DisputeListModel>()
    var selectedDisputeName = MutableLiveData<String>()
    var loadingProgress = MutableLiveData<Boolean>()
    var errorResponse = MutableLiveData<String>()
    var historyModelLiveData = MutableLiveData<HistoryDetailModel>()
    var serviceType = MutableLiveData<String>()
    var disputeType = MutableLiveData<String>()
    var disputeName = MutableLiveData<String>()
    var userID = MutableLiveData<String>()
    var providerID = MutableLiveData<String>()
    var storeID = MutableLiveData<String>()
    var requestID = MutableLiveData<String>()
    var selectedDisputeModel = MutableLiveData<DisputeListData>()
    var disputeID = MutableLiveData<String>()
    var postDisputeLiveData = MutableLiveData<DisputeStatus>()
    var disputeStatusLiveData = MutableLiveData<DisputeStatusModel>()

    fun moveToMainActivity() = navigator.goBack()

    fun getTransportHistoryDetail(selectedID: String) {
        getCompositeDisposable().add(appRepository
                .getTransPortDetail(this, Constants.M_TOKEN +
                        preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), selectedID))
    }

    fun getServiceHistoryDetail(selectedID: String) {
        getCompositeDisposable().addAll(appRepository.getServiceDetail(this, Constants.M_TOKEN +
                preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), selectedID))
    }

    fun getOrderHistoryDetail(selectedID: String) {
        getCompositeDisposable().addAll(appRepository.getOrderDetail(this, Constants.M_TOKEN +
                preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), selectedID))
    }

    fun showDisputeList() = navigator.showDisputeList()

    fun getDisputeList() {
        loadingProgress.value = true
        Log.e("servicetype", "----------" + serviceType.value)
        if (serviceType.value.equals("transport")) {
            getCompositeDisposable().add(appRepository.getDisputeList(this, Constants.M_TOKEN +
                    preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, "")))
        } else {
            getCompositeDisposable().add(appRepository.getDisputeList(this, Constants.M_TOKEN +
                    preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), serviceType.value!!))
        }
    }

    fun postTaxiDispute(params: HashMap<String, String>) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.addTaxiDispute(this, Constants.M_TOKEN +
                preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), params))
    }

    fun postServiceDispute(params: HashMap<String, String>, requestID: String) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.addServiceDispute(this, Constants.M_TOKEN +
                preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), requestID, params))
    }

    fun postOrderDispute(params: HashMap<String, String>) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.addOrderDispute(this, Constants.M_TOKEN +
                preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), params))
    }

    fun getTransPortDisputeStatus(requestID: String) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.getTransportDisputeStatus(this,
                Constants.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), requestID))
    }

    fun getOrderDisputeStatus(requestID: String) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.getOrderDisputeStatus(this,
                Constants.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), requestID))
    }

    fun setSelectedValue(SelectedData: String) {
        selectedDisputeName.value = SelectedData
    }

    fun viewReceipt() = navigator.onClickViewReceipt()

    fun lossItem() = navigator.onClickLossItem()

    fun cancel() = navigator.onClickCancelBtn()
}