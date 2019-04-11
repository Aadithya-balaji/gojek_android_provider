package com.xjek.provider.views.wallet

import android.view.View

interface  WalletNavigator{
    fun showErrorMsg(error:String)
    fun  validate():Boolean
    fun  addAmount(view: View)
}