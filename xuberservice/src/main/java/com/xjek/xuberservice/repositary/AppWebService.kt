package com.xjek.xuberservice.repositary

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface AppWebService{


    @GET("provider/check/request")
    fun xuperCheckRequest(@Query("latitude") latitude:String,@Query("longitude") longitude:String)


    @Multipart
    @POST("provider/update/serve/request")
    fun xuperUpdateServcie(@Header("Authorization") token:String,@PartMap params:HashMap<String,RequestBody>,  @Part image: MultipartBody.Part)

    @FormUrlEncoded
    @POST("provider/rate/serve")
    fun xuperRating(@Header("Authorization") token:String,@FieldMap params:HashMap<String,String>)

}
