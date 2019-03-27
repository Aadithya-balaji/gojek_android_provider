package com.appoets.gojek.provider.views.TransactionStatusActivity

import com.appoets.base.base.BaseViewModel

open class TransactionStatusViewModel : BaseViewModel<TransactionStatusNavigator>()
{

    fun showStatus()
    {
        navigator.showStatus()
    }
}
