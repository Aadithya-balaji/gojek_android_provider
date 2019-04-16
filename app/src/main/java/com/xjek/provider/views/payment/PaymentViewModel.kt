package com.xjek.provider.views.payment

import com.xjek.base.base.BaseViewModel
import com.xjek.xjek.ui.payment.PaymentNavigator

public class PaymentViewModel : BaseViewModel<PaymentNavigator>() {
    fun addAmount() {
        navigator.addWalletAmount()
    }

    fun openTransactionStatusActivty() {
        navigator.goToTransactionStatusActivty()
    }

    fun addNewCard(){
        navigator.addCard()
    }



}