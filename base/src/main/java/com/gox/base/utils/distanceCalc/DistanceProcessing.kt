package com.gox.base.utils.distanceCalc

import android.os.AsyncTask
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class DistanceProcessing(private val mListener: DistanceListener) :
        AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg url: String): String {
        var data = ""
        try {
            data = DistanceCalc().downloadData(url[0])
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
                jsonObject.has("error_message") ->
                    mListener.whenDirectionFail(jsonObject.optString("error_message"))
                jsonObject.optString("status") == "OK" ->
                    mListener.whenDone(Gson().fromJson(result, DistanceCalcModel::class.java))
                else -> mListener.whenDirectionFail(jsonObject.optString("status"))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            mListener.whenDirectionFail("Google API call failed")
        }
    }
}