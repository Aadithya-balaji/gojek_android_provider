package com.xjek.provider.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.xjek.base.data.PreferencesHelper.message


class  WalletTransactionList{
    @SerializedName("statusCode")
    @Expose
    private var statusCode: String? = null
    @SerializedName("title")
    @Expose
    private var title: String? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("responseData")
    @Expose
    private var responseData: TranscationModel? = null
    @SerializedName("error")
    @Expose
    private var error: List<Any>? = null

    fun getStatusCode(): String? {
        return statusCode
    }

    fun setStatusCode(statusCode: String) {
        this.statusCode = statusCode
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getResponseData(): TranscationModel? {
        return responseData
    }

    fun setResponseData(responseData: TranscationModel) {
        this.responseData = responseData
    }

    fun getError(): List<Any>? {
        return error
    }

    fun setError(error: List<Any>) {
        this.error = error
    }

}