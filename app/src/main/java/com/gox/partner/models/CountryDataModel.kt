package com.gox.partner.models

import com.google.gson.annotations.SerializedName

data class CountryDataModel(
        var id: String = "",
        @SerializedName("country_name")
        var CountryName: String = ""
)