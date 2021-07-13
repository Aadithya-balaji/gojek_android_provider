package com.gox.taxiservice.model

data class RentalPackageDetails(
    val created_at: String?,
    val created_type: String?,
    val hour: String,
    val id: Int,
    val km: String,
    val price: Int,
    val ride_city_price_id: Int?,
    var isSelected: Boolean = false
)