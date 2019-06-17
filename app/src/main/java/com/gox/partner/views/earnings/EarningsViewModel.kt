package com.gox.partner.views.earnings

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.EarningsResponse
import com.gox.partner.repository.AppRepository

class EarningsViewModel : BaseViewModel<EarningsNavigator>() {

    val mRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()

    val earnings = MutableLiveData<EarningsResponse>()

    fun earnings(userId: Int) {
        loadingProgress.value = true
        getCompositeDisposable().add(mRepository.getEarnings(object : ApiListener {
            override fun success(successData: Any) {
                earnings.value = successData as EarningsResponse
                loadingProgress.value = false
            }

            override fun fail(failData: Throwable) {
                loadingProgress.value = false
            }
        }, userId))
    }
}