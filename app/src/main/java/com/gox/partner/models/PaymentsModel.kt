package com.gox.partner.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentsModel {
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("credentials")
    @Expose
    private var credentials: CredentialsModel? = null

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun getCredentials(): CredentialsModel? {
        return credentials
    }

    fun setCredentials(credentials: CredentialsModel) {
        this.credentials = credentials
    }


}
