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
            @SerializedName("appsetting") val appSetting: AppSetting
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
                val payments: List<Payments>,
                @SerializedName("cmspage") val cmsPage: CmsPage,
                @SerializedName("supportdetails") val supportDetails: SupportDetails,
                val languages: List<Language>
        ) {
            data class Payments(
                    val name: String,
                    val status: String,
                    val credentials: List<Credentials>
            ) {
                data class Credentials(
                        val name: String,
                        val value: String
                )
            }

            data class CmsPage(
                    @SerializedName("privacypolicy") val privacyPolicy: String,
                    val help: String,
                    val terms: String,
                    val cancel: String
            )

            data class SupportDetails(
                    @SerializedName("contact_number") val contactNumber: List<ContactNumber>,
                    @SerializedName("contact_email") val contactEmail: String
            ) {
                data class ContactNumber(
                        val number: String
                )
            }

            data class Language(
                    val name: String,
                    val key: String
            )
        }
    }
}