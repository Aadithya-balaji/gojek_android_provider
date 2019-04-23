package com.xjek.provider.models

data class BankTemplateModel(
        val error: List<Any> = listOf(),
        val message: String = "",
        val responseData: List<ResponseData> = listOf(),
        val statusCode: String = "",
        val title: String = ""
) {
    data class ResponseData(
            val bankdetails: Any? = Any(),
            val company_id: Int = 0,
            val country_id: Int = 0,
            val id: Int = 0,
            val label: String = "",
            val max: Int = 0,
            val min: Int = 0,
            val type: String = ""
    )
}