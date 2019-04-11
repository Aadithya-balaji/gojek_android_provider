package com.xjek.provider.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class  TransactionDatum{
    @SerializedName("id")
    @Expose
    private var id: Int? = null
    @SerializedName("provider_id")
    @Expose
    private var providerId: Int? = null
    @SerializedName("transaction_id")
    @Expose
    private var transactionId: Int? = null
    @SerializedName("company_id")
    @Expose
    private var companyId: Int? = null
    @SerializedName("transaction_alias")
    @Expose
    private var transactionAlias: Any? = null
    @SerializedName("transaction_desc")
    @Expose
    private var transactionDesc: Any? = null
    @SerializedName("type")
    @Expose
    private var type: String? = null
    @SerializedName("amount")
    @Expose
    private var amount: Double? = null
    @SerializedName("open_balance")
    @Expose
    private var openBalance: Int? = null
    @SerializedName("close_balance")
    @Expose
    private var closeBalance: Int? = null
    @SerializedName("payment_log")
    @Expose
    private var paymentLog: PaymentLog? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getProviderId(): Int? {
        return providerId
    }

    fun setProviderId(providerId: Int?) {
        this.providerId = providerId
    }

    fun getTransactionId(): Int? {
        return transactionId
    }

    fun setTransactionId(transactionId: Int?) {
        this.transactionId = transactionId
    }

    fun getCompanyId(): Int? {
        return companyId
    }

    fun setCompanyId(companyId: Int?) {
        this.companyId = companyId
    }

    fun getTransactionAlias(): Any? {
        return transactionAlias
    }

    fun setTransactionAlias(transactionAlias: Any) {
        this.transactionAlias = transactionAlias
    }

    fun getTransactionDesc(): Any? {
        return transactionDesc
    }

    fun setTransactionDesc(transactionDesc: Any) {
        this.transactionDesc = transactionDesc
    }

    fun getType(): String? {
        return type
    }

    fun setType(type: String) {
        this.type = type
    }

    fun getAmount(): Double? {
        return amount
    }

    fun setAmount(amount: Double?) {
        this.amount = amount
    }

    fun getOpenBalance(): Int? {
        return openBalance
    }

    fun setOpenBalance(openBalance: Int?) {
        this.openBalance = openBalance
    }

    fun getCloseBalance(): Int? {
        return closeBalance
    }

    fun setCloseBalance(closeBalance: Int?) {
        this.closeBalance = closeBalance
    }

    fun getPaymentLog(): PaymentLog? {
        return paymentLog
    }

    fun setPaymentLog(paymentLog: PaymentLog) {
        this.paymentLog = paymentLog
    }

}