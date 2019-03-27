package com.appoets.gojek.taxiservice.views.views.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryListResponseModel {
    @SerializedName("statusCode")
    @Expose
    var statusCode: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("responseData")
    @Expose
    var responseData: List<CountryResponseModel>? = null
    @SerializedName("error")
    @Expose
    var error: List<Any>? = null
}
