package com.xjek.provider.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class  WalletModel{
    @SerializedName("wallet_balance")
    @Expose
    private var walletBalance: Int? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null

    fun getWalletBalance(): Int? {
        return walletBalance
    }

    fun setWalletBalance(walletBalance: Int?) {
        this.walletBalance = walletBalance
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }
}