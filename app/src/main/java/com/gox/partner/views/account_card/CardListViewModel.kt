package com.gox.partner.views.account_card

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.AddCardModel
import com.gox.partner.models.CardListModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository

class CardListViewModel : BaseViewModel<CardListNavigator>() {
    var appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()
    var selectedCardID = MutableLiveData<String>()
    var selectedStripeID = MutableLiveData<String>()
    var addCardLiveResposne = MutableLiveData<AddCardModel>()
    var deleCardLivResponse = MutableLiveData<AddCardModel>()
    var cardListLiveResponse = MutableLiveData<CardListModel>()
    var amount = MutableLiveData<String>()
    var resources: Resources? = null

    init {
        this.resources = resources
    }

    fun saveCard() {
        navigator.addCard()
    }

    fun deselectCard() {
        navigator.deselectCard()
    }

    fun amountAdd(view: View) {
        navigator.addAmount(view)
    }

    fun removeCard(){
        navigator.removeCard()
    }
    fun cardDeselect() {
        navigator.deselectCard()
    }

    fun getCardList() {
        loadingProgress?.let { it.value = true }
        getCompositeDisposable().add(appRepository.getCardList(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), "100", "1"))
    }


    fun callAddCardApi(stripeID: String) {
        loadingProgress?.let { it.value = true }
        val params = HashMap<String, String>()
        params.put(WebApiConstants.addCard.STRIP_TOKEN, stripeID)
        getCompositeDisposable().add(appRepository.addCard(this, params, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
    }

    fun callCardDeleteCardAPi() {
        loadingProgress?.let { it.value = true }
        if (!selectedCardID.value.isNullOrEmpty())
            getCompositeDisposable().add(appRepository.deleteCDard(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), selectedCardID.value!!))
        else
            navigator.showErrorMsg(resources!!.getString(com.gox.partner.R.string.empty_card))
    }
}