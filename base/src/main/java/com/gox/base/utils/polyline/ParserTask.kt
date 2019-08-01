package com.gox.base.utils.polyline

import android.graphics.Color
import android.os.AsyncTask
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class ParserTask internal constructor(private val mPolyLineListener: PolyLineListener)
    : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {

    private var error: String? = null

    override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>>? {

        val jObject: JSONObject
        var routes: List<List<HashMap<String, String>>>? = null

        try {
            jObject = JSONObject(jsonData[0])
            val parser = DirectionsJSONParser()
            routes = parser.parse(jObject)
            if (routes.isEmpty()) error = jObject.toString()
            else getDistance(jObject)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return routes
    }

    override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
        var points: ArrayList<LatLng>
        var lineOptions: PolylineOptions? = null

        for (i in result.indices) {
            points = ArrayList()
            lineOptions = PolylineOptions()

            val path = result[i]

            for (j in path.indices) {
                val point = path[j]

                val lat = java.lang.Double.parseDouble(Objects.requireNonNull<String>(point["lat"]))
                val lng = java.lang.Double.parseDouble(Objects.requireNonNull<String>(point["lng"]))
                val position = LatLng(lat, lng)

                points.add(position)
            }

            lineOptions.addAll(points)
            lineOptions.width(12f)
            lineOptions.color(Color.RED)
            lineOptions.geodesic(true)
        }

        if (lineOptions != null) mPolyLineListener.whenDone(lineOptions)
        else mPolyLineListener.whenFail(error!!)
    }

    private fun getDistance(jObject: JSONObject){
        val jRoutes: JSONArray
        var jLegs: JSONArray
        var jDistance: JSONObject
        var distance: Double
        var jDuration: JSONObject
        var duration: Double

        try {

            jRoutes = jObject.getJSONArray("routes")

            for (i in 0 until jRoutes.length()) {
                jLegs = (jRoutes.get(i) as JSONObject).getJSONArray("legs")
                for (j in 0 until jLegs.length()) {
                    jDistance = (jLegs.get(j) as JSONObject).getJSONObject("distance")
                    distance = jDistance.getDouble("value")
                    println("RRR :: distance = $distance")

                    jDuration = (jLegs.get(j) as JSONObject).getJSONObject("duration")
                    duration = jDuration.getDouble("value")
                    println("RRR :: duration = $duration")

                    mPolyLineListener.getDistanceTime(distance, duration)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}