package com.xjek.provider.models

import com.google.gson.annotations.SerializedName
import com.xjek.base.models.CommonResponse

data class DocumentTypeResponseModel(
        val responseData: List<ResponseData>,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    data class ResponseData(
            val id: Int,
            @SerializedName("company_id") val companyId: Int,
            val service: Any,
            val name: String,
            val type: String,
            @SerializedName("file_type") val fileType: String,
            @SerializedName("is_backside") val isBackside: String,
            @SerializedName("is_expire") val isExpire: Int,
            val status: Int,
            @SerializedName("provider_document") val providerDocument: ProviderDocument?
    ) {
        data class ProviderDocument(
                val id: Int,
                @SerializedName("provider_id") val providerId: Int,
                @SerializedName("document_id") val documentId: Int,
                @SerializedName("company_id") val companyId: Int,
                val url: List<Url>,
                @SerializedName("unique_id") val uniqueId: Any,
                val status: String,
                @SerializedName("expires_at") val expiresAt: String
        ) {
            data class Url(
                    val url: String
            )
        }
    }
}