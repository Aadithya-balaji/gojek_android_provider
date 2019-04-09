package com.xjek.provider.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentsModel {
    @SerializedName("card")
    @Expose
    var card: String? = null
    @SerializedName("stripe_secret_key")
    @Expose
    var stripeSecretKey: String? = null
    @SerializedName("stripe_publishable_key")
    @Expose
    var stripePublishableKey: String? = null
    @SerializedName("cash")
    @Expose
    var cash: String? = null
    @SerializedName("stripe_currency")
    @Expose
    var stripeCurrency: String? = null

}
