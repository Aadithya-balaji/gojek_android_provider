package com.gox.taxiservice.repositary

import com.gox.base.utils.distanceCalc.DistanceCalcModel
import com.gox.taxiservice.model.*
import io.reactivex.Observable
import retrofit2.http.*

interface TaxiWebService {

    @GET("provider/check/ride/request")
    fun taxiCheckRequestAPI(
            @Header("Authorization") token: String
//            @Query("latitude") lat: Double,
//            @Query("longitude") lon: Double
    ): Observable<CheckRequestModel>

    @FormUrlEncoded
    @POST("provider/cancel/ride/request")
    fun cancelRequest(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<CancelRequestModel>

    @GET("provider/reasons?type=TRANSPORT")
    fun taxiGetReason(@Header("Authorization") token: String):
            Observable<ReasonModel>

    @FormUrlEncoded
    @POST("provider/waiting")
    fun waitingTime(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<WaitingTime>

    @FormUrlEncoded
    @POST("provider/update/ride/request")
    fun taxiStatusUpdate(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<CheckRequestModel>

    @POST("provider/update/ride/request")
    fun taxiDroppingStatus(
            @Header("Authorization") token: String,
            @Body request: DroppedStatusModel
    ): Observable<CheckRequestModel>

    @FormUrlEncoded
    @POST("provider/transport/payment")
    fun confirmPayment(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<PaymentModel>

    @FormUrlEncoded
    @POST("provider/rate/ride")
    fun submitRating(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<TaxiRatingResponse>

}