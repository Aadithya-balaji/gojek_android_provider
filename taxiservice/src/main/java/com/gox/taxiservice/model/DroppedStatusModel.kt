package com.gox.taxiservice.model

import java.io.Serializable

data class DroppedStatusModel(
        var id: String = "",
        var _method: String = "",
        var status: String = "",
        var toll_price: String = "",

        var distance: Double = 0.0,
        var latitude: Double = 0.0,
        var location_points: List<LocationPoint> = listOf(),
        var longitude: Double = 0.0
) : Serializable

data class LocationPoint(
        var lat: Double = 0.0,
        var lng: Double = 0.0
) : Serializable