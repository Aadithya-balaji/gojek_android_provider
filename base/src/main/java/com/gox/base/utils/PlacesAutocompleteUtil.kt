
package com.gox.base.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.gox.base.R
import com.gox.base.base.BaseApplication
import com.gox.base.data.PlacesModel
import com.gox.base.data.PreferencesKey
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.util.*

class PlacesAutocompleteUtil(context: Context) {

    private var placesClient: PlacesClient

    init {
        Places.initialize(context, context.getText(R.string.google_map_key).toString() as String)
        placesClient = Places.createClient(context)
    }

    var mPrimary: String? = null
    var mSecondary: String? = null
    var mFullAddress: String? = null

    var mLatitude: Double = 0.0
    var mLongitude: Double = 0.0

   private var mPlaceFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)!!

    fun findAutocompletePredictions(placesQuery: String, code: String, listener: PlaceSuggestion) {
        println("RRR :: placesQuery = ${placesQuery}")
        val mPlacesList: ArrayList<PlacesModel>? = ArrayList()
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
//                .setTypeFilter(TypeFilter.GEOCODE)
                .setSessionToken(token)
                .setCountry(code)
                .setQuery(placesQuery)
                .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            mPlacesList!!.clear()
            for (prediction in response.autocompletePredictions) {
                mPlacesList.add(PlacesModel(prediction.placeId,
                        prediction.getPrimaryText(null).toString(),
                        prediction.getSecondaryText(null).toString(),
                        prediction.getFullText(null).toString()))

                mPrimary = prediction.getPrimaryText(null).toString()
                mSecondary = prediction.getSecondaryText(null).toString()
                mFullAddress = prediction.getFullText(null).toString()

                println("RRR :: mFullAddress = ${mFullAddress}")
                println("RRR :: mPlacesList = ${mPlacesList.size}")
                listener.onPlaceReceived(mPlacesList)
            }
        }.addOnFailureListener { e ->
            e.printStackTrace()
            if (e is ApiException) println("RRR :: e = ${e.message}")
        }
    }

    fun getPlaceName(placeId: String): LatLng {
        val request = FetchPlaceRequest.builder(placeId, mPlaceFields).build()
        var location = LatLng(mLatitude, mLongitude)
        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            println("RRR :: place.latLng = ${place.latLng}")
            location = place.latLng!!
            mLatitude = place.latLng!!.latitude
            mLongitude = place.latLng!!.longitude

        }.addOnFailureListener { e ->
            e.printStackTrace()
            if (e is ApiException) println("RRR :: e = ${e.message}")
        }
        return location
    }
    interface  onLoccationListner{
        fun onLatLngRecived(placeLatLng:LatLng)
    }

    fun getPlaceName(placeId: String,onLatLngListner:onLoccationListner): LatLng {
        var location = LatLng(mLatitude, mLongitude)
        try {
            val request = FetchPlaceRequest.builder(placeId, mPlaceFields).build()
            placesClient.fetchPlace(request).addOnSuccessListener { response ->
                val place = response.place
                println("RRR :: place.latLng = ${place.latLng}")
                location = place.latLng!!
                mLatitude = place.latLng!!.latitude
                mLongitude = place.latLng!!.longitude
                onLatLngListner.onLatLngRecived(location)
                Log.e("PlaceLocation","----------------"+location.toString())

            }.addOnFailureListener { e ->
                e.printStackTrace()
                Log.e("PlaceLocation","----------------"+e.printStackTrace())
            }

        }catch (e: Exception){
            e.printStackTrace()
        }

        return  location
    }


    interface PlaceSuggestion {
        fun onPlaceReceived(mPlacesList: ArrayList<PlacesModel>?)
    }

    fun getCurrentAddress(addressLatLng: LatLng,mapkey:String) {
        val url = "https://maps.googleapis.com/maps/api/geocode/json?key=" + mapkey + "&latlng=" + addressLatLng.latitude + "," + addressLatLng.longitude + "&sensor=true"
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("ADDRESS_LIST", "SIZE" + e.localizedMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                val str_response = response.body?.string()
                //creating json object
                val json_contact: JSONObject = JSONObject(str_response)
                //creating json array
                val jsonarray_info: JSONArray = json_contact.getJSONArray("results")
                var i: Int = 0
                val size: Int = jsonarray_info.length()
                Log.d("ADDRESS_LIST", "SIZE" + size)
                for (i in 0 until size) {
                    val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)
                    if (i == 0) {
                        Log.d("ADDRESS1", "ADDRESS1" + json_objectdetail.get("formatted_address"))
                    }


                }

            }
        })

    }

}