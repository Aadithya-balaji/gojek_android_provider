package com.xjek.provider.models

data class ListDocumentResponse(
        val error: List<Any> = listOf(),
        val message: String = "",
        val responseData: List<ResponseData> = listOf(),
        val statusCode: String = "",
        val title: String = ""
) {
    data class ResponseData(
            val company_id: Int = 0,
            val file_type: String = "",
            val id: Int = 0,
            val is_backside: Any? = Any(),
            val is_expire: String = "",
            val name: String = "",
            val provider_document: ProviderDocument? = ProviderDocument(),
            val service: Any? = Any(),
            val status: Int = 0,
            val type: String = ""
    ) {
        data class ProviderDocument(
                val company_id: Int = 0,
                val document_id: Int = 0,
                val expires_at: String = "",
                val id: Int = 0,
                val provider_id: Int = 0,
                val status: String = "",
                val unique_id: Any? = Any(),
                val url: List<Url> = listOf()
        ) {
            data class Url(
                    val url: String = ""
            )
        }
    }
}