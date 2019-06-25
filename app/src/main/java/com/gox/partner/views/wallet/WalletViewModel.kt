package com.gox.partner.views.wallet

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import com.gox.partner.R
import com.gox.partner.models.ProfileResponse
import com.gox.partner.models.WalletResponse
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository

class WalletViewModel : BaseViewModel<WalletNavigator>() {

    var showLoading = MutableLiveData<Boolean>()
    var walletAmount = MutableLiveData<String>()
    var walletLiveResponse = MutableLiveData<WalletResponse>()
    var selectedStripeID = MutableLiveData<String>()
    var resources: Resources? = null
    val mRepository = AppRepository.instance()
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
            getCompositeDisposable().add(mRepository.addWalletAmount(object : ApiListener {
                override fun success(successData: Any) {
                    walletLiveResponse.value = successData as WalletResponse
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMsg(getErrorMessage(failData))
                }
            }, params))
        }
    }

    fun getProfile() {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.getProfile(object : ApiListener {
            override fun success(successData: Any) {
                mProfileResponse.value = successData as ProfileResponse
            }

            override fun fail(failData: Throwable) {
                navigator.showErrorMsg(getErrorMessage(failData))
            }
        }))
    }
}