package com.gox.base.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationPointsEntity(
        @PrimaryKey(autoGenerate = true) var id: Int?,
        var lat: Double,
        var lng: Double,
        var time: String
)