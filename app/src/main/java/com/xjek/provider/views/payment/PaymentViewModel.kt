package com.xjek.xjek.ui.payment

import com.xjek.base.base.BaseViewModel

public class PaymentViewModel : BaseViewModel<PaymentNavigator>() {
    fun addAmount() {
        navigator.addWalletAmount()
    }

    fun openTransactionStatusActivty() {
        navigator.goToTransactionStatusActivty()
    }
}