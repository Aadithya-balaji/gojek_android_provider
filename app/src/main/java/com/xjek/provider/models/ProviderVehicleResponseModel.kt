package com.xjek.provider.models

import android.os.Parcel
import com.google.gson.annotations.SerializedName
import com.xjek.base.extensions.KParcelable
import com.xjek.base.extensions.parcelableCreator

data class ProviderVehicleResponseModel(
        val id: Int,
        @SerializedName("provider_id") val providerId: Int,
        @SerializedName("vehicle_service_id") val vehicleServiceId: Int,
        @SerializedName("vehicle_year") val vehicleYear: Int,
        @SerializedName("vehicle_color") val vehicleColor: String?,
        @SerializedName("vehicle_make") val vehicleMake: String?,
        @SerializedName("company_id") val companyId: Int,
        @SerializedName("vehicle_model") val vehicleModel: String?,
        @SerializedName("vehicle_no") val vehicleNo: String?,
        @SerializedName("vechile_image") val vehicleImage: String?,
        val picture: String?,
        val picture1: String?
) : KParcelable {

    private constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeInt(id)
        writeInt(providerId)
        writeInt(vehicleServiceId)
        writeInt(vehicleYear)
        writeString(vehicleColor)
        writeString(vehicleMake)
        writeInt(companyId)
        writeString(vehicleModel)
        writeString(vehicleNo)
        writeString(vehicleImage)
        writeString(picture)
        writeString(picture1)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::ProviderVehicleResponseModel)
    }
}