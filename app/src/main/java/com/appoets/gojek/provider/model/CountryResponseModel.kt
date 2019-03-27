package com.appoets.gojek.taxiservice.views.views.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryResponseModel {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("country_name")
    @Expose
    var countryName: String? = null
    @SerializedName("country_code")
    @Expose
    var countryCode: String? = null
    @SerializedName("country_phonecode")
    @Expose
    var countryPhonecode: String? = null
    @SerializedName("country_currency")
    @Expose
    var countryCurrency: Any? = null
    @SerializedName("country_symbol")
    @Expose
    var countrySymbol: Any? = null
    @SerializedName("status")
    @Expose
    var status: Any? = null
    @SerializedName("timezone")
    @Expose
    var timezone: Any? = null

}
