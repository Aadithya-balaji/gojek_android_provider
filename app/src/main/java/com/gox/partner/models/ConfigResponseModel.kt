package com.gox.partner.models

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
        var admin_service: String = "",
        var base_url: String = "",
        var display_name: String = "",
        var id: Int = 0,
        var status: Int = 0
)

data class Appsetting(
        var android_key: String = "",
        var demo_mode: Int = 0,
        var provider_negative_balance: Long = 0,
        var cmspage: Cmspage = Cmspage(),
        var ios_key: String = "",
        var languages: List<Language> = listOf(),
        var otp_verify: Int = 0,
        val send_sms: Int = 0,
        val send_email: Int = 0,
        var ride_otp: Int = 0,
        var order_otp: Int = 0,
        var service_otp: Int = 0,
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