package com.xjek.provider.views.wallet

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.CardListModel
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant

class WalletViewModel : BaseViewModel<WalletNavigator>() {
    var cardResponseData = MutableLiveData<CardListModel>()
    var loadingProgress = MutableLiveData<Boolean>()
    var walletAmount = MutableLiveData<String>()

    val appRepository = AppRepository.instance()

    fun getCardList() {
        getCompositeDisposable().add(appRepository.getCardList(this, "Bearer "
                + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
    }

    fun amountAdd(view: View) {
        navigator.addAmount(view)
    }

    fun addWalletAmount() {
        if (navigator.validate()) {
            val params = HashMap<String, String>()
            params.put(WebApiConstants.AddWallet.AMOUNT, walletAmount.value.toString())
            params.put(WebApiConstants.AddWallet.CARD_ID, Constant.CARD_ID)
            params.put(WebApiConstants.AddWallet.USER_TYPE, Constant.TYPE_PROVIDER)
            params.put(WebApiConstants.AddWallet.PAYMENT_MODE, "card")
            getCompositeDisposable().add(appRepository.addWalletAmount(this, params, "Bearer "
                    + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
        }
    }

}