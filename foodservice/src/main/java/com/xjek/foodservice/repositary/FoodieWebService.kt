package com.xjek.foodservice.repositary

import com.xjek.foodservice.model.FoodieCheckRequestModel
import com.xjek.foodservice.model.FoodieRatingRequestModel
import io.reactivex.Observable
import retrofit2.http.*

interface FoodieWebService {

    @GET("provider/check/order/request")
    fun foodieCheckRequest(@Header("Authorization") token: String/*, @Query("latitude") latitude: String, @Query("longitude") longitude: String*/): Observable<FoodieCheckRequestModel>

    @FormUrlEncoded
    @POST("provider/update/order/request")
    fun foodieUpdateRequest(@Header("Authorization") token: String, @FieldMap params: HashMap<String, String>): Observable<FoodieCheckRequestModel>

    @FormUrlEncoded
    @POST("provider/rate/order")
    fun foodieRatingRequest(@Header("Authorization") token: String, @FieldMap params: HashMap<String, String>): Observable<FoodieRatingRequestModel>
}
