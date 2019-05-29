package com.gox.partner.models

data class CategoriesResponseModel(
        val error: List<Any> = listOf(),
        val message: String = "",
        val responseData: List<ResponseData> = listOf(),
        val statusCode: String = "",
        val title: String = ""
) {
    data class ResponseData(
            val company_id: Int? = 0,
            val id: Int? = 0,
            val picture: String = "",
            val price_choose: String = "",
            val providerservicecategory: List<Any> = listOf(),
            val service_category_name: String = "",
            val service_category_order: Int? = 0,
            val service_category_status: Int? = 0
    )
}