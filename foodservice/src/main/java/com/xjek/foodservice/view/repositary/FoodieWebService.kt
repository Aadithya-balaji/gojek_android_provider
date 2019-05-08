package com.xjek.foodservice.view.repositary

import com.xjek.foodservice.view.model.FoodieCheckRequestModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FoodieWebService {

    @GET("provider/check/order/request")
    fun foodieCheckRequest(@Header("Authorization") token: String, @Query("latitude") latitude: String, @Query("longitude") longitude: String): Observable<FoodieCheckRequestModel>
}
