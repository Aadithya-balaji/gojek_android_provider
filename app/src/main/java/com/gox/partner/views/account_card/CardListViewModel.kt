package com.gox.partner.views.account_card

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.AddCardModel
import com.gox.partner.models.CardListModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository

class CardListViewModel : BaseViewModel<CardListNavigator>() {

    var mRepository = AppRepository.instance()

    var showLoading = MutableLiveData<Boolean>()
    var selectedCardID = MutableLiveData<String>()
    var selectedStripeID = MutableLiveData<String>()
    var addCardLiveResponse = MutableLiveData<AddCardModel>()
    var deleteCardLivResponse = MutableLiveData<AddCardModel>()
    var cardListLiveResponse = MutableLiveData<CardListModel>()
    var resources: Resources? = null

    init {
        this.resources = resources
    }

    fun saveCard() = navigator.addCard()

    fun removeCard() = navigator.removeCard()

    fun cardDeselect() = navigator.deselectCard()

    fun getCardList() {
        if (BaseApplication.isNetworkAvailable) {
            showLoading.value = true
            getCompositeDisposable().add(mRepository.getCardList(object : ApiListener {
                override fun success(successData: Any) {
                    cardListLiveResponse.value = successData as CardListModel
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMsg(getErrorMessage(failData))
                }
            }, "100", "1"))
        }
    }

    fun callAddCardApi(stripeID: String) {
        if (BaseApplication.isNetworkAvailable) {
            showLoading.value = true
            val params = HashMap<String, String>()
            params[WebApiConstants.addCard.STRIP_TOKEN] = stripeID
            getCompositeDisposable().add(mRepository.addCard(object : ApiListener {
                override fun success(successData: Any) {
                    addCardLiveResponse.value = successData as AddCardModel
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMsg(getErrorMessage(failData))
                }
            }, params))
        }
    }

    fun callCardDeleteCardAPi() {
        if (BaseApplication.isNetworkAvailable) {
            showLoading.value = true
            if (!selectedCardID.value.isNullOrEmpty())
                getCompositeDisposable().add(mRepository.deleteCard(object : ApiListener {
                    override fun success(successData: Any) {
                        deleteCardLivResponse.value = successData as AddCardModel
                    }

                    override fun fail(failData: Throwable) {
                        navigator.showErrorMsg(getErrorMessage(failData))
                    }
                }, selectedCardID.value!!))
            else navigator.showErrorMsg(resources!!.getString(com.gox.partner.R.string.empty_card))
        }
    }
}