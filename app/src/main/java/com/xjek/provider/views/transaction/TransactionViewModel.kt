package com.xjek.provider.views.transaction

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.TranscationModel
import com.xjek.provider.models.WalletTransactionList
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant

class TransactionViewModel:BaseViewModel<TransactionNavigator>(){
    var transcationLiveResponse=MutableLiveData<WalletTransactionList>()
    val appRepository=AppRepository.instance()
    fun callTranscationApi(){
       getCompositeDisposable().add(appRepository.getTransaction(this,"Bearer "+Constant.accessToken))
    }
}

