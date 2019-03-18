package com.appoets.xjek.ui.payment

import com.appoets.basemodule.base.BaseViewModel

public class PaymentViewModel : BaseViewModel<PaymentNavigator>() {
    fun addAmount() {
        navigator.addWalletAmount()
    }

    fun openTransactionStatusActivty() {
        navigator.goToTransactionStatusActivty()
    }
}