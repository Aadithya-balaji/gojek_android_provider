package com.gox.partner.models

import com.google.gson.annotations.SerializedName

data class CityDataModel(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("city_name")
        var cityName: String? = null
)