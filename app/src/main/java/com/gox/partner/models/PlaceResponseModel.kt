package com.gox.partner.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PlaceResponseModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("state_id")
    @Expose
    var stateId: Int? = null
    @SerializedName("city_name")
    @Expose
    var cityName: String? = null
    @SerializedName("status")
    @Expose
    var status: Any? = null
    @SerializedName("state_name")
    @Expose
    @get:SerializedName("country_symbol")
    var stateName: String? = null
    @SerializedName("timezone")
    @Expose
    var timezone: Any? = null
    @SerializedName("country_id")
    @Expose
    var countryid: Int? = 0
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
}
