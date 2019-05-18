package com.xjek.provider.views.history_details

interface CurrentOrderDetailsNavigator {
    fun goBack()
    fun onClickDispute()
    fun onClickViewRecepit()
    fun onClickLossItem()
    fun onClickCancelBtn()
    fun showDisputeList()
    fun showErrorMessage(error:String)
}