package com.gox.partner.views.transaction

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.WalletTransaction
import com.gox.partner.repository.AppRepository

class TransactionViewModel : BaseViewModel<TransactionNavigator>() {

    val mRepository = AppRepository.instance()
    var transactionLiveResponse = MutableLiveData<WalletTransaction>()

    var errorResponse = MutableLiveData<String>()
    var showLoading = MutableLiveData<Boolean>()

    fun callTransactionApi() {
        getCompositeDisposable().add(mRepository.getTransaction(object : ApiListener {
            override fun success(successData: Any) {
                transactionLiveResponse.value = successData as WalletTransaction
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
            }
        }))
    }
}

