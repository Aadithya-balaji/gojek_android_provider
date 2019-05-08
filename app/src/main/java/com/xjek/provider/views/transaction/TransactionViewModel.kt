package com.xjek.provider.views.transaction

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.WalletTransactionList
import com.xjek.provider.repository.AppRepository

class TransactionViewModel : BaseViewModel<TransactionNavigator>() {
    var transcationLiveResponse = MutableLiveData<WalletTransactionList>()
    var errorResponse = MutableLiveData<String>()
    val appRepository = AppRepository.instance()
    fun callTranscationApi() {
        getCompositeDisposable().add(appRepository.getTransaction(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
    }
}

