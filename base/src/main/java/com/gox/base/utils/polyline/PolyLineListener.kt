package com.gox.base.utils.polyline

import com.google.android.gms.maps.model.PolylineOptions

interface PolyLineListener {

    fun whenDone(output: PolylineOptions)

    fun whenFail(statusCode: String)
}