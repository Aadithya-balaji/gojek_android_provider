package com.xjek.taxiservice.repositary

import com.xjek.taxiservice.model.CheckRequestModel
import io.reactivex.Observable
import retrofit2.http.*

interface AppWebService {

    @GET("provider/check/ride/request")
    fun taxiCheckRequestAPI(
            @Header("Authorization") token: String,
            @Query("latitude") lat: String,
            @Query("longitude") lon: String
    ): Observable<CheckRequestModel>

    @FormUrlEncoded
    @POST("provider/update/ride/request")
    fun taxiStatusUpdate(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<CheckRequestModel>

}