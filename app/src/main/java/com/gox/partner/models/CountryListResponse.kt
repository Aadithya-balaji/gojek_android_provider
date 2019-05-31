package com.gox.partner.models

import java.io.Serializable

data class CountryListResponse(
        val error: List<Any>,
        val message: String,
        val responseData: List<CountryResponseData>,
        val statusCode: String,
        val title: String
) : Serializable

data class CountryResponseData(
        val city: List<City>,
        val country_code: String,
        val country_currency: Any,
        val country_name: String,
        val country_phonecode: String,
        val country_symbol: Any,
        val id: Int,
        val status: Any,
        val timezone: Any
) : Serializable

data class City(
        val city_name: String,
        val country_id: Int,
        val id: Int,
        val state_id: Int,
        val status: Any
) : Serializable