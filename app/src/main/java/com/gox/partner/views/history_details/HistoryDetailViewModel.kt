package com.gox.partner.views.history_details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import com.gox.partner.models.*
import com.gox.partner.repository.AppRepository

class HistoryDetailViewModel : BaseViewModel<CurrentOrderDetailsNavigator>() {

    private val mRepository = AppRepository.instance()
    var transportDetail = MutableLiveData<HistoryDetailModel.ResponseData.Transport>()
    var orderDetail = MutableLiveData<HistoryDetailModel.ResponseData.Order>()
    var serviceDetail = MutableLiveData<HistoryDetailModel.ResponseData.Service>()
    var disputeListData = MutableLiveData<DisputeListModel>()
    var selectedDisputeName = MutableLiveData<String>()
    var showLoading = MutableLiveData<Boolean>()
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
    var serviceName:String?=""

    fun moveToMainActivity() = navigator.goBack()

    fun getTransportHistoryDetail(selectedID: String) {
        getCompositeDisposable().add(mRepository.getTransPortDetail(object : ApiListener {
            override fun success(successData: Any) {
                historyModelLiveData.value = successData as HistoryDetailModel
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, selectedID))
    }

    fun getServiceHistoryDetail(selectedID: String) {
        getCompositeDisposable().addAll(mRepository.getServiceDetail(object : ApiListener {
            override fun success(successData: Any) {
                historyModelLiveData.value = successData as HistoryDetailModel
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, selectedID))
    }

    fun getOrderHistoryDetail(selectedID: String) {
        getCompositeDisposable().addAll(mRepository.getOrderDetail(object : ApiListener {
            override fun success(successData: Any) {
                historyModelLiveData.value = successData as HistoryDetailModel
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, selectedID))
    }

    fun showDisputeList() = navigator.showDisputeList()

    fun getDisputeList() {
        showLoading.value = true
        Log.e("servicetype", "----------" + serviceType.value)
        if(serviceType.value.equals(Constants.ModuleTypes.SERVICE,true)){
            serviceName="services"
        }else{
            serviceName=serviceType.value!!.toLowerCase()
        }

        if (serviceName.equals(Constants.ModuleTypes.TRANSPORT, true)) {
            getCompositeDisposable().add(mRepository.getDisputeList(object : ApiListener {
                override fun success(successData: Any) {
                    disputeListData.value = successData as DisputeListModel
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    errorResponse.value = getErrorMessage(failData)
                    showLoading.postValue(false)
                }
            }))
        } else {
            getCompositeDisposable().add(mRepository.getDisputeList(object : ApiListener {
                override fun success(successData: Any) {
                    disputeListData.value = successData as DisputeListModel
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    errorResponse.value = getErrorMessage(failData)
                    showLoading.postValue(false)
                }
            }, serviceName!!))
        }
    }

    fun postTaxiDispute(params: HashMap<String, String>) {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.addTaxiDispute(object : ApiListener {
            override fun success(successData: Any) {
                postDisputeLiveData.value = successData as DisputeStatus
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, params))
    }

    fun postServiceDispute(params: HashMap<String, String>) {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.addServiceDispute(object : ApiListener {
            override fun success(successData: Any) {
                postDisputeLiveData.value = successData as DisputeStatus
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, params))
    }

    fun postOrderDispute(params: HashMap<String, String>) {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.addOrderDispute(object : ApiListener {
            override fun success(successData: Any) {
                postDisputeLiveData.value = successData as DisputeStatus
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, params))
    }

    fun getTransPortDisputeStatus(requestID: String) {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.getTransportDisputeStatus(object : ApiListener {
            override fun success(successData: Any) {
                disputeStatusLiveData.value = successData as DisputeStatusModel
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, requestID))
    }

    fun getOrderDisputeStatus(requestID: String) {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.getOrderDisputeStatus(object : ApiListener {
            override fun success(successData: Any) {
                disputeStatusLiveData.value = successData as DisputeStatusModel
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, requestID))
    }


    fun getServiceDisputeStatus(requestID: String) {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.getServiceDisputeStatus(object : ApiListener {
            override fun success(successData: Any) {
                disputeStatusLiveData.value = successData as DisputeStatusModel
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
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