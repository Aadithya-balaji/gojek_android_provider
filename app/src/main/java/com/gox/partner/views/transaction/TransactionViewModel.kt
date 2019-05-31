package com.gox.partner.views.transaction

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.WalletTransactionList
import com.gox.partner.repository.AppRepository

class TransactionViewModel : BaseViewModel<TransactionNavigator>() {
    var transcationLiveResponse = MutableLiveData<WalletTransactionList>()
    var errorResponse = MutableLiveData<String>()
    val appRepository = AppRepository.instance()
    var showLoading=MutableLiveData<Boolean>()
    fun callTranscationApi() {
        getCompositeDisposable().add(appRepository.getTransaction(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
    }
}

