package com.gox.partner.views.history_details

interface CurrentOrderDetailsNavigator {
    fun goBack()
    fun onClickViewReceipt()
    fun onClickLossItem()
    fun onClickCancelBtn()
    fun showDisputeList()
    fun showErrorMessage(error: String)
}