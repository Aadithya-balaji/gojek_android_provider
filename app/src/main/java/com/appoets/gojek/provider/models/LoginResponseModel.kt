package com.appoets.gojek.provider.models

import com.appoets.base.models.CommonResponse
import com.google.gson.annotations.SerializedName

data class LoginResponseModel(
        val responseData: ResponseData,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            @SerializedName("token_type")
            val tokenType: String,
            @SerializedName("expires_in")
            val expiresIn: Int,
            @SerializedName("access_token")
            val accessToken: String,
            val user: User
    ) {
        data class User(
                val id: Int,
                @SerializedName("first_name")
                val firstName: String,
                @SerializedName("last_name")
                val last_name: String,
                @SerializedName("payment_mode")
                val paymentMode: String,
                val email: String,
                @SerializedName("country_code")
                val countryCode: String,
                val mobile: String,
                val gender: String,
                @SerializedName("device_token")
                val deviceToken: String,
                @SerializedName("device_id")
                val deviceId: String,
                @SerializedName("device_type")
                val deviceType: String,
                @SerializedName("login_by")
                val loginBy: String,
                @SerializedName("social_unique_id")
                val socialUniqueId: String,
                val latitude: Double,
                val longitude: Double,
                @SerializedName("stripe_acc_id")
                val stripeAccId: String,
                @SerializedName("wallet_balance")
                val walletBalance: Int,
                val rating: Int,
                val status: String,
                @SerializedName("admin_id")
                val adminId: Any,
                @SerializedName("payment_gateway_id")
                val paymentGatewayId: String,
                val otp: Any,
                val language: String,
                val picture: String,
                @SerializedName("referral_unique_id")
                val referralUniqueId: String,
                @SerializedName("qrcode_url")
                val qrcodeUrl: String,
                @SerializedName("country_id")
                val countryId: Any,
                @SerializedName("city_id")
                val cityId: Int,
                @SerializedName("email_verified_at")
                val emailVerifiedAt: Any,
                @SerializedName("created_type")
                val createdType: Any,
                @SerializedName("created_by")
                val createdBy: Any,
                @SerializedName("modified_type")
                val modifiedType: Any,
                @SerializedName("modified_by")
                val modifiedBy: Any,
                @SerializedName("deleted_type")
                val deletedType: Any,
                @SerializedName("deleted_by")
                val deletedBy: Any,
                @SerializedName("company_id")
                val companyId: Int,
                @SerializedName("state_id")
                val stateId: Any
        )
    }
}