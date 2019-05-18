package com.xjek.provider.models

data class DisputeStatus(
        val error: List<Any?>? = listOf(),
        val message: String? = "",
        val responseData: List<Any?>? = listOf(),
        val statusCode: String? = "",
        val title: String? = ""
)