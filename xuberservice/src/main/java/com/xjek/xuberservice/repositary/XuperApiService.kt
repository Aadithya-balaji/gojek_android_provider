package com.xjek.xuberservice.repositary

import com.xjek.xuberservice.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface XuperApiService{


    @GET("provider/check/serve/request")
    fun xuperCheckRequest(@Header("Authorization") token:String,@Query("latitude") latitude:String,@Query("longitude") longitude:String):Observable<XuperCheckRequest>

    @Multipart
    @POST("provider/update/serve/request")
    fun xuperUpdateServcie(@Header("Authorization") token:String,@PartMap params:HashMap<String,RequestBody>,  @Part image: MultipartBody.Part):Observable<UpdateRequest>

    @FormUrlEncoded
    @POST("provider/rate/serve")
    fun xuperRating(@Header("Authorization") token:String,@FieldMap params:HashMap<String,String>):Observable<XuperRatingModel>

    @GET("provider/reasons")
    fun getReasons(@Header("Authorization") token:String,type:String):Observable<ReasonModel>

    @FormUrlEncoded
    @POST("provider/cancel/serve/request")
    fun cancelRequest(@Header("Authorization") token: String,params:HashMap<String,String>):Observable<CancelRequestModel>

}
