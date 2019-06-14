package com.gox.foodservice.repositary

import com.gox.foodservice.model.FoodieCheckRequestModel
import com.gox.foodservice.model.FoodieRatingRequestModel
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodieWebService {

    @GET("provider/check/order/request")
    fun foodieCheckRequest(): Observable<FoodieCheckRequestModel>

    @FormUrlEncoded
    @POST("provider/update/order/request")
    fun foodieUpdateRequest(@FieldMap params: HashMap<String, String>): Observable<FoodieCheckRequestModel>

    @FormUrlEncoded
    @POST("provider/rate/order")
    fun foodieRatingRequest(@FieldMap params: HashMap<String, String>): Observable<FoodieRatingRequestModel>

}
