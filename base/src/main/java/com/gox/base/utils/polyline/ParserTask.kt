package com.gox.base.utils.polyline

import android.graphics.Color
import android.os.AsyncTask
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import java.util.*
import kotlin.Exception as Exception1

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

}