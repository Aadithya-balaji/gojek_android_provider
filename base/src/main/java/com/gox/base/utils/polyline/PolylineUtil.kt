package com.gox.base.utils.polyline

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class PolylineUtil(private val mPolyLineListener: PolyLineListener) :
        AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg url: String): String {
        var data = ""
        try {
            data = DirectionUtils().downloadUrl(url[0])
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return data
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        try {
            val jsonObject = JSONObject(result)
            longLog(jsonObject.toString(), "RRR :: jsonObject = ")
            when {
                jsonObject.has("error_message") -> mPolyLineListener.whenFail(jsonObject.optString("error_message"))
                jsonObject.optString("status") == "OK" -> ParserTask(mPolyLineListener).execute(result)
                else -> mPolyLineListener.whenFail(jsonObject.optString("status"))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun longLog(str: String, s: String) {
        if (str.length > 4000) {
            Log.d(s, str.substring(0, 4000))
            longLog(str.substring(4000), s)
        } else Log.d(s, str)
    }
}