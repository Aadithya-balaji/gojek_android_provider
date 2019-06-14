package com.gox.partner.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WalletModel {
    @SerializedName("wallet_balance")
    @Expose
    private var walletBalance: Double? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null

    fun getWalletBalance(): Double? {
        return walletBalance
    }

    fun setWalletBalance(walletBalance: Double?) {
        this.walletBalance = walletBalance
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }
}