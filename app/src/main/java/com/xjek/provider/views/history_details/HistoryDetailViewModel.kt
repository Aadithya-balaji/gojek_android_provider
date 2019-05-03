package com.xjek.provider.views.history_details

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.provider.model.DisputeListModel
import com.xjek.provider.model.DisputeStatusModel
import com.xjek.provider.model.HistoryDetailModel
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

    fun moveToMainActivity() {
        navigator.goBack()
    }

    fun getHistoryDeatail(selectedID: String) {
        getCompositeDisposable().add(appRepository
                .getHistoryDetail(this
                        , Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""),
                        selectedID))
    }

    fun getUpcomingHistoryDeatail(selectedID: String) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository
                .getUpcomingHistoryDetail(this
                        , Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""),
                        selectedID))
    }

    fun getDisputeList() {

        loadingProgress.value = true
        getCompositeDisposable().add(appRepository
                .getDisputeList(this
                        , Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, "")
                ))
    }

    fun addDispute(id: String, providerid: String, selectedDispute: String?) {

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("id", id)
        hashMap.put("dispute_type", "user")
        hashMap.put("provider_id", providerid)
        hashMap.put("dispute_name", selectedDispute!!)

        loadingProgress.value = true
        getCompositeDisposable().add(appRepository
                .addDispute(this
                        , Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), hashMap
                ))
    }

    fun getDisputeStatus(id: Int) {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository
                .getDisputeStatus(this
                        , Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, "")
                        , id.toString()
                ))
    }

    fun addLossItem(id: Int, lostitem: Editable?) {

       /* val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("id", id.toString())
        hashMap.put("lost_item_name", lostitem.toString())

        loadingProgress.value = true
        getCompositeDisposable().add(appRepository
                .addLostItem(this
                        , Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), hashMap
                ))*/
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