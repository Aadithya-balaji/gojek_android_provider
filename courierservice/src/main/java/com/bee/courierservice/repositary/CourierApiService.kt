package com.bee.courierservice.repositary

import com.bee.courierservice.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CourierApiService {

    @GET("provider/check/delivery/request")
    fun xUberCheckRequest(
            @Query("latitude") latitude: String,
            @Query("longitude") longitude: String
    ): Observable<CourierCheckRequest>

    @Multipart
    @POST("provider/update/delivery/request")
    fun xUberUpdateServices(
            @PartMap params: HashMap<String, RequestBody>,
            @Part frontImage: MultipartBody.Part?,
            @Part backImage: MultipartBody.Part?
    ): Observable<CourierCheckRequest>

    @FormUrlEncoded
    @POST("provider/rate/delivery")
    fun xUberRating(
            @FieldMap params: HashMap<String, String>
    ): Observable<CourierRatingModel>

    @GET("provider/reasons")
    fun getReasons(
            @Query("type") type: String
    ): Observable<ReasonModel>

    @FormUrlEncoded
    @POST("provider/cancel/delivery/request")
    fun cancelRequest(
            @FieldMap params: HashMap<String, String>
    ): Observable<CancelRequestModel>

    @Multipart
    @POST("provider/delivery/payment")
    fun confirmPayment(
            @PartMap params: HashMap<String, String>
    ): Observable<PaymentModel>
}
