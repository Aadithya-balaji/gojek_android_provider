package com.gox.partner.models

import com.google.gson.annotations.SerializedName

data class StatusResponseDataModel(
        @SerializedName("provider_status")
        var providerStatus: String? = ""
)