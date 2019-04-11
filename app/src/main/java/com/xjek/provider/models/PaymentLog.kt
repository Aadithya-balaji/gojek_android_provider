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
    @SerializedName("transaction_id")
    @Expose
    private var transactionId: Any? = null
    @SerializedName("response")
    @Expose
    private var response: String? = null
    @SerializedName("created_type")
    @Expose
    private var createdType: String? = null
    @SerializedName("created_by")
    @Expose
    private var createdBy: Int? = null
    @SerializedName("modified_type")
    @Expose
    private var modifiedType: String? = null
    @SerializedName("modified_by")
    @Expose
    private var modifiedBy: Int? = null
    @SerializedName("deleted_type")
    @Expose
    private var deletedType: Any? = null
    @SerializedName("deleted_by")
    @Expose
    private var deletedBy: Any? = null
    @SerializedName("created_at")
    @Expose
    private var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    private var updatedAt: String? = null

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

    fun getTransactionId(): Any? {
        return transactionId
    }

    fun setTransactionId(transactionId: Any) {
        this.transactionId = transactionId
    }

    fun getResponse(): String? {
        return response
    }

    fun setResponse(response: String) {
        this.response = response
    }

    fun getCreatedType(): String? {
        return createdType
    }

    fun setCreatedType(createdType: String) {
        this.createdType = createdType
    }

    fun getCreatedBy(): Int? {
        return createdBy
    }

    fun setCreatedBy(createdBy: Int?) {
        this.createdBy = createdBy
    }

    fun getModifiedType(): String? {
        return modifiedType
    }

    fun setModifiedType(modifiedType: String) {
        this.modifiedType = modifiedType
    }

    fun getModifiedBy(): Int? {
        return modifiedBy
    }

    fun setModifiedBy(modifiedBy: Int?) {
        this.modifiedBy = modifiedBy
    }

    fun getDeletedType(): Any? {
        return deletedType
    }

    fun setDeletedType(deletedType: Any) {
        this.deletedType = deletedType
    }

    fun getDeletedBy(): Any? {
        return deletedBy
    }

    fun setDeletedBy(deletedBy: Any) {
        this.deletedBy = deletedBy
    }

    fun getCreatedAt(): String? {
        return createdAt
    }

    fun setCreatedAt(createdAt: String) {
        this.createdAt = createdAt
    }

    fun getUpdatedAt(): String? {
        return updatedAt
    }

    fun setUpdatedAt(updatedAt: String) {
        this.updatedAt = updatedAt
    }
}