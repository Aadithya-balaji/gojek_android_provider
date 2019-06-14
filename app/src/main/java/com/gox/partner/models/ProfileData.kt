package com.gox.partner.models

import com.google.gson.annotations.SerializedName

class ProfileData {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("first_name")
    var firstName: String? = null
    @SerializedName("last_name")
    var lastName: String? = null
    @SerializedName("payment_mode")
    var paymentMode: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("country_code")
    var countryCode: String? = null
    @SerializedName("mobile")
    var mobile: String? = null
    @SerializedName("gender")
    var gender: String? = null
    @SerializedName("device_token")
    var deviceToken: String? = null
    @SerializedName("device_type")
    var deviceType: String? = null
    @SerializedName("login_by")
    var loginBy: String? = null
    @SerializedName("social_unique_id")
    var socialUniqueId: String? = null
    @SerializedName("latitude")
    var latitude: Any? = null
    @SerializedName("longitude")
    var longitude: Any? = null
    @SerializedName("stripe_acc_id")
    var stripeAccountId: String? = null
    @SerializedName("wallet_balance")
    var walletBalance: Double? = 0.0
    @SerializedName("rating")
    var rating: Double? = 0.0
    @SerializedName("status")
    var status: String? = null
    @SerializedName("admin_id")
    var adminId: Any? = null
    @SerializedName("picture")
    var picture: String? = null
    @SerializedName("referral")
    val referralData: ReferralDataModel? = null
    @SerializedName("country")
    val countryName: CountryDataModel? = null
    @SerializedName("city")
    val cityName: CityDataModel? = null
}