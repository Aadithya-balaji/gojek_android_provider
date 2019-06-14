package com.gox.partner.views.wallet

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.R
import com.gox.partner.models.ProfileResponse
import com.gox.partner.models.WalletResponse
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository

class WalletViewModel(res: Resources) : BaseViewModel<WalletNavigator>() {

    var loadingProgress = MutableLiveData<Boolean>()
    var walletAmount = MutableLiveData<String>()
    var walletLiveResponse = MutableLiveData<WalletResponse>()
    var selectedStripeID = MutableLiveData<String>()
    var resources: Resources? = null
    var showLoading = MutableLiveData<Boolean>()
    val appRepository = AppRepository.instance()
    var mProfileResponse = MutableLiveData<ProfileResponse>()

    fun amountAdd(view: View) = navigator.addAmount(view)

    fun addWalletAmount() {
        if (walletAmount.value.isNullOrEmpty())
            navigator.showErrorMsg(resources!!.getString(R.string.empty_wallet_amount))
        else
            navigator.getCard()
    }

    fun callAddAmtApi() {
        if (navigator.validate()) {
            showLoading.value = true
            val params = HashMap<String, String>()
            params[WebApiConstants.AddWallet.AMOUNT] = walletAmount.value.toString()
            params[WebApiConstants.AddWallet.CARD_ID] = selectedStripeID.value.toString()
            params[WebApiConstants.AddWallet.USER_TYPE] = Constants.TYPE_PROVIDER
            params[WebApiConstants.AddWallet.PAYMENT_MODE] = "card"
            getCompositeDisposable().add(appRepository.addWalletAmount(this, params,
                    "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
        }
    }

    fun getProfile() {
        showLoading.value = true
        getCompositeDisposable().add(appRepository.getProfile(this,
                "Bearer" + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
    }
}