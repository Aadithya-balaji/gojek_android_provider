package com.gox.partner.models

import android.os.Parcel
import com.google.gson.annotations.SerializedName
import com.gox.base.extensions.KParcelable
import com.gox.base.extensions.parcelableCreator

data class ProviderVehicleResponseModel(
        var id: Int,
        @SerializedName("provider_id") var providerId: Int,
        @SerializedName("vehicle_service_id") var vehicleServiceId: Int,
        @SerializedName("vehicle_year") var vehicleYear: Int,
        @SerializedName("vehicle_color") var vehicleColor: String?,
        @SerializedName("vehicle_make") var vehicleMake: String?,
        @SerializedName("company_id") var companyId: Int,
        @SerializedName("vehicle_model") var vehicleModel: String?,
        @SerializedName("vehicle_no") var vehicleNo: String?,
        @SerializedName("vechile_image") var vehicleImage: String?,
        @SerializedName("wheel_chair") var wheelChair: Int,
        @SerializedName("child_seat") var childSeat: Int,
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
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()
            )

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
        writeInt(wheelChair)
        writeInt(childSeat)
        writeString(picture)
        writeString(picture1)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::ProviderVehicleResponseModel)
    }
}