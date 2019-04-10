package com.xjek.provider.models

data class Model(
        val statusCode: String,
        val title: String,
        val message: String,
        val responseData: ResponseData,
        val error: List<Any>
) {
    data class ResponseData(
            val baseUrl: String,
            val services: List<Service>,
            val appsetting: Appsetting
    ) {
        data class Appsetting(
                val referral: Int,
                val socialLogin: Int,
                val otpVerify: Int,
                val payments: Payments,
                val cmspage: Cmspage,
                val supportdetails: Supportdetails,
                val languages: List<Language>
        ) {
            data class Payments(
                    val card: String,
                    val stripeSecretKey: String,
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

        data class Service(
                val id: Int,
                val adminServiceName: String,
                val displayName: String,
                val baseUrl: String,
                val status: Int
        )
    }
}