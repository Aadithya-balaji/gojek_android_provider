package com.gox.partner.models

//data class ConfigResponseModel(
//        val responseData: ResponseData,
//        override val statusCode: String,
//        override val title: String,
//        override val message: String,
//        override val error: List<Any>
//) : CommonResponse {
//    data class ResponseData(
//            @SerializedName("base_url") val baseUrl: String,
//            var services: List<Service>,
//            @SerializedName("appsetting") val appSetting: AppSetting
//    ) {
//        data class Service(
//                val id: Int,
//                @SerializedName("admin_service_name") val adminServiceName: String,
//                @SerializedName("display_name") val displayName: String,
//                @SerializedName("base_url") var baseUrl: String,
//                val status: Int
//        )
//
//        data class AppSetting(
//                val referral: Int,
//                @SerializedName("social_login") val socialLogin: Int,
//                @SerializedName("otp_verify") val otpVerify: Int,
//                val payments: List<Payments>,
//                @SerializedName("cmspage") val cmsPage: CmsPage,
//                @SerializedName("supportdetails") val supportDetails: SupportDetails,
//                val languages: List<Language>
//        ) {
//            data class Payments(
//                    val name: String,
//                    val status: String,
//                    val credentials: List<Credentials>
//            ) {
//                data class Credentials(
//                        val name: String,
//                        val value: String
//                )
//            }
//
//            data class CmsPage(
//                    @SerializedName("privacypolicy") val privacyPolicy: String,
//                    val help: String,
//                    val terms: String,
//                    val cancel: String
//            )
//
//            data class SupportDetails(
//                    @SerializedName("contact_number") val contactNumber: List<ContactNumber>,
//                    @SerializedName("contact_email") val contactEmail: String
//            ) {
//                data class ContactNumber(
//                        val number: String
//                )
//            }
//
//            data class Language(
//                    val name: String,
//                    val key: String
//            )
//        }
//    }
//}

data class ConfigResponseModel(
        var error: List<Any> = listOf(),
        var message: String = "",
        var responseData: ConfigResponseData = ConfigResponseData(),
        var statusCode: String = "",
        var title: String = ""
)

data class ConfigResponseData(
        var appsetting: Appsetting = Appsetting(),
        var base_url: String = "",
        var services: List<ConfigService> = listOf()
)

data class ConfigService(
        var admin_service_name: String = "",
        var base_url: String = "",
        var display_name: String = "",
        var id: Int = 0,
        var status: Int = 0
)

data class Appsetting(
        var android_key: String = "",
        var cmspage: Cmspage = Cmspage(),
        var ios_key: String = "",
        var languages: List<Language> = listOf(),
        var otp_verify: Int = 0,
        var payments: List<ConfigPayment> = listOf(),
        var referral: Int = 0,
        var social_login: Int = 0,
        var supportdetails: Supportdetails = Supportdetails()
)

data class Cmspage(
        var cancel: String = "",
        var help: String = "",
        var privacypolicy: String = "",
        var terms: String = ""
)

data class Supportdetails(
        var contact_email: String = "",
        var contact_number: List<ContactNumber> = listOf()
)

data class ContactNumber(
        var number: String = ""
)

data class ConfigPayment(
        var credentials: List<Credential> = listOf(),
        var name: String = "",
        var status: String = ""
)

data class Credential(
        var name: String = "",
        var value: String = ""
)

data class Language(
        var key: String = "",
        var name: String = ""
)