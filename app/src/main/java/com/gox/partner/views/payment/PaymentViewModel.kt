package com.gox.partner.views.payment

import com.gox.base.base.BaseViewModel
import com.gox.xjek.ui.payment.PaymentNavigator

public class PaymentViewModel : BaseViewModel<PaymentNavigator>() {
    fun addAmount() {
        navigator.addWalletAmount()
    }

    fun openTransactionStatusActivty() {
        navigator.goToTransactionStatusActivty()
    }

    fun addNewCard() {
        navigator.addCard()
    }


}