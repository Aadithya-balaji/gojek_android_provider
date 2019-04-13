package com.xjek.provider.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class  PaymentLog{

    @SerializedName("id")
    @Expose
    private var id: Int? = null
    @SerializedName("company_id")
    @Expose
    private var companyId: Int? = null
    @SerializedName("is_wallet")
    @Expose
    private var isWallet: Int? = null
    @SerializedName("user_type")
    @Expose
    private var userType: String? = null
    @SerializedName("payment_mode")
    @Expose
    private var paymentMode: String? = null
    @SerializedName("user_id")
    @Expose
    private var userId: Int? = null
    @SerializedName("amount")
    @Expose
    private var amount: Int? = null
    @SerializedName("transaction_code")
    @Expose
    private var transactionCode: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getCompanyId(): Int? {
        return companyId
    }

    fun setCompanyId(companyId: Int?) {
        this.companyId = companyId
    }

    fun getIsWallet(): Int? {
        return isWallet
    }

    fun setIsWallet(isWallet: Int?) {
        this.isWallet = isWallet
    }

    fun getUserType(): String? {
        return userType
    }

    fun setUserType(userType: String) {
        this.userType = userType
    }

    fun getPaymentMode(): String? {
        return paymentMode
    }

    fun setPaymentMode(paymentMode: String) {
        this.paymentMode = paymentMode
    }

    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }

    fun getAmount(): Int? {
        return amount
    }

    fun setAmount(amount: Int?) {
        this.amount = amount
    }

    fun getTransactionCode(): String? {
        return transactionCode
    }

    fun setTransactionCode(transactionCode: String) {
        this.transactionCode = transactionCode
    }
}