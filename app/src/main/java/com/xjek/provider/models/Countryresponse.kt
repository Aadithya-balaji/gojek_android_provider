package com.xjek.provider.models


data class CountryListResponse(
        val error: List<Any>,
        val message: String,
        val responseData: List<CountryResponseData>,
        val statusCode: String,
        val title: String
)

data class CountryResponseData(
        val country_code: String,
        val country_currency: Any,
        val country_name: String,
        val country_phonecode: String,
        val country_symbol: Any,
        val id: Int,
        val status: Any,
        val timezone: Any
)