package com.gox.base.utils.distanceCalc

import com.google.android.gms.maps.model.LatLng

class DistanceUtils {

    fun getUrl(points: MutableList<LatLng>, key: String): String {
        val strOrigin = points[0].latitude.toString() + "," + points[0].longitude
        val strDest = points[points.size - 1].latitude.toString() + "," + points[points.size - 1].longitude
        val wayPoints = StringBuilder()

        for (i in 1 until points.size - 1)
            if (i == 1) wayPoints.append(points[i].latitude).append(",").append(points[i].longitude)
            else wayPoints.append("|").append(points[i].latitude).append(",").append(points[i].longitude)

        val url = " https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + strOrigin +
                "&destination=" + strDest +
                "&waypoints=" + wayPoints +
                "&key=" + key

        println("RRR Google distance URL = $url")

        return url
    }

}