package com.gox.partner.views.history_details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.*
import com.gox.partner.repository.AppRepository

class HistoryDetailViewModel : BaseViewModel<CurrentOrderDetailsNavigator>() {

    private val mRepository = AppRepository.instance()

    var historyDetailResponse = MutableLiveData<HistoryDetailModel>()
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
        getCompositeDisposable().add(mRepository.getTransPortDetail(object : ApiListener {
            override fun success(successData: Any) {
                historyModelLiveData.value = successData as HistoryDetailModel
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }, selectedID))
    }

    fun getServiceHistoryDetail(selectedID: String) {
        getCompositeDisposable().addAll(mRepository.getServiceDetail(object : ApiListener {
            override fun success(successData: Any) {
                historyModelLiveData.value = successData as HistoryDetailModel
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }, selectedID))
    }

    fun getOrderHistoryDetail(selectedID: String) {
        getCompositeDisposable().addAll(mRepository.getOrderDetail(object : ApiListener {
            override fun success(successData: Any) {
                historyModelLiveData.value = successData as HistoryDetailModel
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }, selectedID))
    }

    fun showDisputeList() = navigator.showDisputeList()

    fun getDisputeList() {
        loadingProgress.value = true
        Log.e("servicetype", "----------" + serviceType.value)
        if (serviceType.value.equals("transport"))
            getCompositeDisposable().add(mRepository.getDisputeList(object : ApiListener {
                override fun success(successData: Any) {
                    disputeListData.value = successData as DisputeListModel
                    loadingProgress.value = false
                }

                override fun fail(failData: Throwable) {
                    errorResponse.value = getErrorMessage(failData)
                    loadingProgress.value = false
                }
            }))
        else getCompositeDisposable().add(mRepository.getDisputeList(object : ApiListener {
            override fun success(successData: Any) {
                disputeListData.value = successData as DisputeListModel
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }, serviceType.value!!))
    }

    fun postTaxiDispute(params: HashMap<String, String>) {
        loadingProgress.value = true
        getCompositeDisposable().add(mRepository.addTaxiDispute(object : ApiListener {
            override fun success(successData: Any) {
                postDisputeLiveData.value = successData as DisputeStatus
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }, params))
    }

    fun postServiceDispute(params: HashMap<String, String>, requestID: String) {
        loadingProgress.value = true
        getCompositeDisposable().add(mRepository.addServiceDispute(object : ApiListener {
            override fun success(successData: Any) {
                postDisputeLiveData.value = successData as DisputeStatus
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }, requestID, params))
    }

    fun postOrderDispute(params: HashMap<String, String>) {
        loadingProgress.value = true
        getCompositeDisposable().add(mRepository.addOrderDispute(object : ApiListener {
            override fun success(successData: Any) {
                postDisputeLiveData.value = successData as DisputeStatus
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }, params))
    }

    fun getTransPortDisputeStatus(requestID: String) {
        loadingProgress.value = true
        getCompositeDisposable().add(mRepository.getTransportDisputeStatus(object : ApiListener {
            override fun success(successData: Any) {
                disputeStatusLiveData.value = successData as DisputeStatusModel
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }, requestID))
    }

    fun getOrderDisputeStatus(requestID: String) {
        loadingProgress.value = true
        getCompositeDisposable().add(mRepository.getOrderDisputeStatus(object : ApiListener {
            override fun success(successData: Any) {
                disputeStatusLiveData.value = successData as DisputeStatusModel
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                loadingProgress.value = false
            }
        }, requestID))
    }

    fun setSelectedValue(SelectedData: String) {
        selectedDisputeName.value = SelectedData
    }

    fun viewReceipt() = navigator.onClickViewReceipt()

    fun lossItem() = navigator.onClickLossItem()

    fun cancel() = navigator.onClickCancelBtn()
}