package com.gox.base.utils

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class CarMarkerAnimUtil {

    fun carAnimWithBearing(marker: Marker, start: LatLng, end: LatLng) {
        val valueAnimator = ValueAnimator.ofFloat(0F, 1F)
        valueAnimator.duration = 1900
        val latLngInterpolator = LatLngInterpolatorNew.LinearFixed()
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { valueAnimator1 ->
            val v = valueAnimator1.animatedFraction
            val newPos = latLngInterpolator.interpolate(v, start, end)
            marker.position = newPos
            marker.setAnchor(0.5f, 0.5f)
            marker.rotation = bearingBetweenLocations(start, end).toFloat()
        }
        valueAnimator.start()
    }

    fun carAnim(marker: Marker, start: LatLng, end: LatLng) {
        val valueAnimator = ValueAnimator.ofFloat(0F, 1F)
        valueAnimator.duration = 1900
        val latLngInterpolator = LatLngInterpolatorNew.LinearFixed()
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { valueAnimator1 ->
            val v = valueAnimator1.animatedFraction
            val newPos = latLngInterpolator.interpolate(v, start, end)
            marker.position = newPos
            marker.setAnchor(0.5f, 0.5f)
            marker.rotation = 0.0F
        }
        valueAnimator.start()
    }

    fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Double {

        val PI = 3.14159
        val lat1 = latLng1.latitude * PI / 180
        val long1 = latLng1.longitude * PI / 180
        val lat2 = latLng2.latitude * PI / 180
        val long2 = latLng2.longitude * PI / 180
        val dLon = long2 - long1
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - (Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon))

        var brng = Math.atan2(y, x)

        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360

        return brng
    }

    interface LatLngInterpolatorNew {
        fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

        class LinearFixed : LatLngInterpolatorNew {
            override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
                val lat = (b.latitude - a.latitude) * fraction + a.latitude
                var lngDelta = b.longitude - a.longitude
                if (Math.abs(lngDelta) > 180) lngDelta -= Math.signum(lngDelta) * 360
                val lng = lngDelta * fraction + a.longitude
                return LatLng(lat, lng)
            }
        }
    }
}