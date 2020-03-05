package com.gox.partner.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ListDocumentResponse(
        val error: List<Any> = listOf(),
        val message: String = "",
        val responseData: List<ResponseData> = listOf(),
        val statusCode: String = "",
        val title: String = ""
) {
    @Parcelize
    data class ResponseData(
            val company_id: Int? = 0,
            val file_type: String = "",
            val id: Int? = 0,
            val is_backside: String?="",
            val is_expire: Int = 0,
            val name: String = "",
            var provider_document: ProviderDocument? = ProviderDocument(),
            val service: String? = "",
            val status: Int? = 0,
            val type: String = ""
    ):Parcelable {
        @Parcelize
        data class ProviderDocument(
                val company_id: Int? = 0,
                val document_id: Int? = 0,
                var expires_at: String = "",
                val id: Int? = 0,
                val provider_id: Int? = 0,
                val status: String = "",
                var url: List<Url> = listOf()
        ) :Parcelable{
            @Parcelize
            data class Url(
                    val url: String = ""
            ):Parcelable
        }
    }
}