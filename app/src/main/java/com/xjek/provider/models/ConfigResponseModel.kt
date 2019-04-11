package com.xjek.provider.models

import com.google.gson.annotations.SerializedName
import com.xjek.base.models.CommonResponse

data class ConfigResponseModel(
        val responseData: ResponseData,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            @SerializedName("base_url") val baseUrl: String,
            val services: List<Service>,
            val appSetting: AppSetting
    ) {
        data class Service(
                val id: Int,
                @SerializedName("admin_service_name") val adminServiceName: String,
                @SerializedName("display_name") val displayName: String,
                @SerializedName("base_url") val baseUrl: String,
                val status: Int
        )

        data class AppSetting(
                val referral: Int,
                @SerializedName("social_login") val socialLogin: Int,
                @SerializedName("otp_verify") val otpVerify: Int,
                val payments: Payments,
                val cmspage: Cmspage,
                val supportdetails: Supportdetails,
                val languages: List<Language>
        ) {
            data class Payments(
                    val card: String,
                    @SerializedName("stripe_secret_key") val stripeSecretKey: String,
                    val stripePublishableKey: String,
                    val cash: String,
                    val stripeCurrency: String
            )

            data class Language(
                    val name: String,
                    val key: String
            )

            data class Cmspage(
                    val privacypolicy: String,
                    val help: String,
                    val terms: String,
                    val cancel: String
            )

            data class Supportdetails(
                    val contactNumber: List<ContactNumber>,
                    val contactEmail: String
            ) {
                data class ContactNumber(
                        val number: String
                )
            }
        }
    }
}