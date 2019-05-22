package com.xjek.provider.views.history_details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.provider.models.DisputeListData
import com.xjek.provider.models.DisputeListModel
import com.xjek.provider.models.DisputeStatusModel
import com.xjek.provider.models.DisputeStatus
import com.xjek.provider.models.HistoryDetailModel
import com.xjek.provider.models.LoginResponseModel
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant

class HistoryDetailViewModel : BaseViewModel<CurrentOrderDetailsNavigator>() {

    private val appRepository = AppRepository.instance()
    val preferenceHelper = PreferencesHelper
    var historyDetailResponse = MutableLiveData<HistoryDetailModel>()
    var historyUpcomingDetailResponse = MutableLiveData<HistoryDetailModel>()
    var disputeListData = MutableLiveData<DisputeListModel>()
    var disputeStatusResponse = MutableLiveData<DisputeStatusModel>()
    var addDisputeResponse = MutableLiveData<LoginResponseModel.ResponseData>()
    var addLostItemResponse = MutableLiveData<LoginResponseModel.ResponseData>()
    var selectedDisputeName = MutableLiveData<String>()
    var loadingProgress = MutableLiveData<Boolean>()
    var errorResponse = MutableLiveData<String>()
    var historyModelLiveData = MutableLiveData<HistoryDetailModel>()
    var serviceType = MutableLiveData<String>()
    var postDisputeLiveData = MutableLiveData<DisputeStatus>()
    var disputeType=MutableLiveData<String>()
    var disputeName=MutableLiveData<String>()
    var userID=MutableLiveData<String>()
    var providerID=MutableLiveData<String>()
    var storeID=MutableLiveData<String>()
    var requestID=MutableLiveData<String>()
    var comments=MutableLiveData<String>()
    var selectedDisputeModel=MutableLiveData<DisputeListData>()
    var  disputeID=MutableLiveData<String>()

    fun moveToMainActivity() {
        navigator.goBack()
    }

    fun getTransportHistoryDetail(selectedID: String) {
        getCompositeDisposable().add(appRepository
                .getTransPortDetail(this, Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), selectedID))
    }

    fun getServiceHistoryDetail(selectedID: String) {
        getCompositeDisposable().addAll(appRepository.getServiceDetail(this, Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), selectedID))
    }

    fun getOrderHistoryDetail(selectedID: String) {
        getCompositeDisposable().addAll(appRepository.getOrderDetail(this, Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), selectedID))
    }


    fun showDisputeList() {
        navigator.showDisputeList()
    }

    fun getDisputeList() {
        loadingProgress.value = true
        Log.e("servicetype", "----------" + serviceType.value)
        if (serviceType.value.equals("transport")) {
            getCompositeDisposable().add(appRepository.getDisputeList(this, Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, "")))
        } else {
            getCompositeDisposable().add(appRepository.getDisputeList(this, Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), serviceType.value!!))
        }
    }


    fun postTaxiDispute(params: HashMap<String, String>) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.addTaxiDispute(this, Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), params))
    }


    fun postServiceDispute(params: HashMap<String, String>, requestID: String) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.addServiceDispute(this, Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), requestID, params))
    }

    fun postOrderDispute(params: HashMap<String, String>) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.addOrderDispute(this, Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), params))
    }

    fun setSelectedValue(Selecteddata: String) {
        selectedDisputeName.value = Selecteddata
    }

    fun getSelectedValue(): MutableLiveData<String> {
        return selectedDisputeName
    }

    fun dispute() {
        navigator.onClickDispute()
    }

    fun viewRecepit() {
        navigator.onClickViewRecepit()
    }

    fun lossItem() {
        navigator.onClickLossItem()
    }

    fun cancel() {
        navigator.onClickCancelBtn()

    }


}