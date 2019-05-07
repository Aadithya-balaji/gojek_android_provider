package com.xjek.provider.views.transaction_status

import com.xjek.base.base.BaseViewModel

open class TransactionStatusViewModel : BaseViewModel<TransactionStatusNavigator>() {

    fun showStatus() {
        navigator.showStatus()
    }
}
