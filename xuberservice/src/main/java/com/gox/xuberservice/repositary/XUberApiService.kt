package com.gox.xuberservice.repositary

import com.gox.xuberservice.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface XUberApiService {

    @GET("provider/check/serve/request")
    fun xUberCheckRequest(
            @Query("latitude") latitude: String,
            @Query("longitude") longitude: String
    ): Observable<XuperCheckRequest>

    @Multipart
    @POST("provider/update/serve/request")
    fun xUberUpdateServices(
            @PartMap params: HashMap<String, RequestBody>,
            @Part frontImage: MultipartBody.Part?,
            @Part backImage: MultipartBody.Part?
    ): Observable<UpdateRequest>

    @FormUrlEncoded
    @POST("provider/rate/serve")
    fun xUberRating(
            @FieldMap params: HashMap<String, String>
    ): Observable<XuperRatingModel>

    @GET("provider/reasons")
    fun getReasons(
            @Query("type") type: String
    ): Observable<ReasonModel>

    @FormUrlEncoded
    @POST("provider/cancel/serve/request")
    fun cancelRequest(
            @FieldMap params: HashMap<String, String>
    ): Observable<CancelRequestModel>

    @Multipart
    @POST("provider/update/serve/request")
    fun confirmPayment(
            @PartMap params: HashMap<String, RequestBody>
    ): Observable<UpdateRequest>
}
