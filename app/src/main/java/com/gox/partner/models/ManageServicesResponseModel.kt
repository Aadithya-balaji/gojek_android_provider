package com.gox.partner.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.gox.base.models.CommonResponse
import kotlinx.android.parcel.Parcelize

data class ManageServicesResponseModel(
        val responseData: List<ResponseData>,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse {
    @Parcelize
    data class ResponseData(
            val id: Int,
            @SerializedName("admin_service") val adminServiceName: String,
            @SerializedName("display_name") val displayName: String,
            @SerializedName("base_url") val baseUrl: String,
            val status: Int,
            @SerializedName("providerservices") val providerServices: ProviderServices,
            val documents: List<ListDocumentResponse.ResponseData> =listOf()

    ) : Parcelable{
        @Parcelize
        data class ProviderServices(
                val id: Int,
                @SerializedName("provider_id") val providerId: Int,
                @SerializedName("admin_service_id") val adminServiceId: Int,
                @SerializedName("category_id") val categoryId: Int,
                @SerializedName("sub_category_id") val subCategoryId: Int,
                @SerializedName("company_id") val companyId: Int,
                @SerializedName("base_fare") val baseFare: Double? = 0.0,
                @SerializedName("per_miles") val perMiles: String,
                @SerializedName("per_mins") val perMins: String,
                val status: String
        ):Parcelable
    }
}