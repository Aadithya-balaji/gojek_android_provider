package com.gox.taxiservice.repositary

import com.gox.taxiservice.model.*
import io.reactivex.Observable
import retrofit2.http.*

interface TaxiWebService {

    @GET("provider/check/ride/request")
    fun taxiCheckRequestAPI(
//            @Query("latitude") lat: Double,
//            @Query("longitude") lon: Double
    ): Observable<CheckRequestModel>

    @FormUrlEncoded
    @POST("provider/cancel/ride/request")
    fun cancelRequest(
            @FieldMap params: HashMap<String, String>
    ): Observable<CancelRequestModel>

    @GET("provider/reasons?type=TRANSPORT")
    fun taxiGetReason(): Observable<ReasonModel>

    @FormUrlEncoded
    @POST("provider/waiting")
    fun waitingTime(
            @FieldMap params: HashMap<String, String>
    ): Observable<WaitingTime>

    @FormUrlEncoded
    @POST("provider/update/ride/request")
    fun taxiStatusUpdate(
            @FieldMap params: HashMap<String, String>
    ): Observable<CheckRequestModel>

    @POST("provider/update/ride/request")
    fun taxiDroppingStatus(
            @Body request: DroppedStatusModel
    ): Observable<CheckRequestModel>

    @FormUrlEncoded
    @POST("provider/transport/payment")
    fun confirmPayment(
            @FieldMap params: HashMap<String, String>
    ): Observable<PaymentModel>

    @FormUrlEncoded
    @POST("provider/rate/ride")
    fun submitRating(
            @FieldMap params: HashMap<String, String>
    ): Observable<TaxiRatingResponse>

}