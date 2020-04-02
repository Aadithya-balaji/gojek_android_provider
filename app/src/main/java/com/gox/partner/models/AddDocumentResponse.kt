package com.gox.partner.models

data class AddDocumentResponse(
        val error: List<Any> = listOf(),
        val message: String = "",
        val responseData: ListDocumentResponse.ResponseData.ProviderDocument = ListDocumentResponse.ResponseData.ProviderDocument(),
        val statusCode: String = "",
        val title: String = ""
) {
    data class ResponseData(
            val company_id: Int? = 0,
            val document_id: String = "",
            val expires_at: String = "",
            val id: Int? = 0,
            val is_document: Int? = 0,
            val provider_id: Int? = 0,
            val status: String = "",
            val url: List<Url> = listOf()
    ) {
        data class Url(
                val url: String = ""
        )
    }
}