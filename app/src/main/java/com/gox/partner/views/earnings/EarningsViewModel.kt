package com.gox.partner.views.earnings

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.EarningsResponse
import com.gox.partner.repository.AppRepository

class EarningsViewModel : BaseViewModel<EarningsNavigator>() {

    val mRepository = AppRepository.instance()
    var showLoading = MutableLiveData<Boolean>()

    val earnings = MutableLiveData<EarningsResponse>()

    fun earnings(userId: Int) {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.getEarnings(object : ApiListener {
            override fun success(successData: Any) {
                earnings.value = successData as EarningsResponse
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                showLoading.postValue(false)
            }
        }, userId))
    }
}