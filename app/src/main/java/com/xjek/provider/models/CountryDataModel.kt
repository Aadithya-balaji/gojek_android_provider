package com.xjek.provider.models

import com.google.gson.annotations.SerializedName

data class CountryDataModel(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("country_name")
        var CountryName: String? = null
)