package com.xjek.provider.views.TransactionStatusActivity

import com.xjek.base.base.BaseViewModel

open class TransactionStatusViewModel : BaseViewModel<TransactionStatusNavigator>()
{

    fun showStatus()
    {
        navigator.showStatus()
    }
}
