package com.xjek.taxiservice.repositary

import com.xjek.taxiservice.model.CheckRequestModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AppWebService {

    @GET("provider/check/ride/request")
    fun taxiCheckRequestAPI(
            @Header("Authorization") token: String,
            @Query("latitude") lat: String,
            @Query("longitude") lon: String
    ): Observable<CheckRequestModel>

}