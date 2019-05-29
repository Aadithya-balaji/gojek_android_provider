package com.xjek.base.utils.distanceCalc

import android.os.AsyncTask
import com.xjek.base.utils.polyline.DirectionUtils
import com.xjek.base.utils.polyline.ParserTask
import com.xjek.base.utils.polyline.PolyLineListener
import org.json.JSONException
import org.json.JSONObject

class DistanceProcessing(private val mListener: PolyLineListener) :
        AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg url: String): String {
        var data = ""
        try {
            data = DistanceData().downloadUrl(url[0])
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return data
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        try {
            val jsonObject = JSONObject(result)
            println("RRR :: jsonObject = $jsonObject")
            when {
                jsonObject.has("error_message") -> mListener.whenFail(jsonObject.optString("error_message"))
                jsonObject.optString("status") == "OK" -> ParserTask(mListener).execute(result)
                else -> mListener.whenFail(jsonObject.optString("status"))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}