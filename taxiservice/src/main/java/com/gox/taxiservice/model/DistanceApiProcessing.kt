package com.gox.taxiservice.model

import java.io.Serializable

data class DistanceApiProcessing(
        var id: Int = 0,
        var apiResponseStatus: String = "",
        var distance: Double = 0.0,
        var reCallAPi: Boolean = false
) : Serializable