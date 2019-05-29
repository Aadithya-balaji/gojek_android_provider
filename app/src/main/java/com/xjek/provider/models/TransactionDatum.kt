package com.xjek.provider.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransactionDatum {
    @SerializedName("id")
    @Expose
    private var id: Int? = 0
    @SerializedName("transaction_id")
    @Expose
    private var transactionId: Int? = 0
    @SerializedName("transaction_desc")
    @Expose
    private var transactionDesc: Any? = null
    @SerializedName("type")
    @Expose
    private var type: String? = ""
    @SerializedName("amount")
    @Expose
    private var amount: Double? =0.0
    @SerializedName("created_at")
    @Expose
    private var createdAt: String? = ""
    @SerializedName("payment_log")
    @Expose
    private var paymentLog: PaymentLog? = null
    @SerializedName("transaction_alias")
    @Expose
    private  var transactionAlias:String=""


    fun  getTransactionAlias():String{
        return transactionAlias
    }


    fun setTransactionAlias(alias:String){
        this.transactionAlias=alias
    }

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getTransactionId(): Int? {
        return transactionId
    }

    fun setTransactionId(transactionId: Int?) {
        this.transactionId = transactionId
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

    fun getCreatedAt(): String? {
        return createdAt
    }

    fun setCreatedAt(createdAt: String) {
        this.createdAt = createdAt
    }

    fun getPaymentLog(): PaymentLog? {
        return paymentLog
    }

    fun setPaymentLog(paymentLog: PaymentLog) {
        this.paymentLog = paymentLog
    }

}