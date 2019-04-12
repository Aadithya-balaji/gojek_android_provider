package com.xjek.provider.views.wallet

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.R
import com.xjek.provider.models.AddCardModel
import com.xjek.provider.models.CardListModel
import com.xjek.provider.models.WalletResponse
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant

class WalletViewModel(res:Resources) : BaseViewModel<WalletNavigator>() {
    var cardResponseData = MutableLiveData<CardListModel>()
    var loadingProgress = MutableLiveData<Boolean>()
    var walletAmount = MutableLiveData<String>()
    var addCardLiveResposne=MutableLiveData<AddCardModel>()
    var deleCardLivResponse=MutableLiveData<AddCardModel>()
    var walletLiveResponse=MutableLiveData<WalletResponse>()
    var selectedCardId=MutableLiveData<String>()
    var resources:Resources?=null
    var showLoading=MutableLiveData<Boolean>()

    init {
        this.resources=resources
    }

    val appRepository = AppRepository.instance()

    fun getCardList() {
        showLoading?.let { it.value=true }
        getCompositeDisposable().add(appRepository.getCardList(this, "Bearer " + Constant.accessToken))
    }

    fun amountAdd(view: View){
        navigator.addAmount(view)
    }

    fun addWalletAmount() {
        if(navigator.validate()){
            showLoading?.let { it.value=true }
            val params=HashMap<String,String>()
            params.put(WebApiConstants.AddWallet.AMOUNT,walletAmount.value.toString())
            params.put(WebApiConstants.AddWallet.CARD_ID,Constant.CARD_ID)
            params.put(WebApiConstants.AddWallet.USER_TYPE,Constant.TYPE_PROVIDER)
            params.put(WebApiConstants.AddWallet.PAYMENT_MODE,"card")
            getCompositeDisposable().add(appRepository.addWalletAmount(this,params,"Bearer "+Constant.accessToken))
        }
    }

    fun callAddCardApi(stripeID:String){
        showLoading?.let { it.value=true }
        val params=HashMap<String,String>()
        params.put(WebApiConstants.addCard.STRIP_TOKEN,stripeID)
        getCompositeDisposable().add(appRepository.addCard(this,params,"Bearer "+Constant.accessToken))
    }

    fun callCardDeleteCardAPi(){
        showLoading?.let { it.value=true }
        if(!selectedCardId.value.isNullOrEmpty())
        getCompositeDisposable().add(appRepository.deleteCDard(this,"Bearer "+Constant.accessToken,selectedCardId.value!!))
        else
          navigator.showErrorMsg(resources!!.getString(R.string.empty_card))
    }

    fun onCardSelected(cardId:String,position:Int){
         navigator.cardPicked(cardId,position)
    }
    fun saveCard(){
        navigator.addCard()
    }

    fun deselectCard(){
        navigator.deselectCard()
    }

}