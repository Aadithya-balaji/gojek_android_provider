package com.xjek.provider.network

import com.xjek.provider.models.*
import com.xjek.user.data.repositary.remote.model.CountryListResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AppWebService {

    @FormUrlEncoded
    @POST("base")
    fun getConfig(@FieldMap params: HashMap<String, String>): Observable<ConfigResponseModel>

    @FormUrlEncoded
    @POST("provider/login")
    fun postLogin(@FieldMap params: HashMap<String, String>): Observable<LoginResponseModel>

    @FormUrlEncoded
    @POST("provider/social/login")
    fun postSocialLogin(@FieldMap params: HashMap<String, String>): Observable<LoginResponseModel>

    @FormUrlEncoded
    @POST("provider/forgot/otp")
    fun postForgotPassword(@FieldMap params: HashMap<String, String>):
            Observable<ForgotPasswordResponseModel>

    @FormUrlEncoded
    @POST("provider/reset/otp")
    fun postResetPassword(@FieldMap params: HashMap<String, String>):
            Observable<ResetPasswordResponseModel>

    @FormUrlEncoded
    @POST("user/countries")
    fun getCountries(@FieldMap params: HashMap<String, Any?>): Observable<CountryListResponse>

    @GET("states/{state_id}")
    fun getStatelist(@Path("state_id") state_id: String): Observable<StateListResponse>

    @GET("cities/{city_id}")
    fun getCityList(@Path("city_id") city_id: String): Observable<CityListResponse>

    @Multipart
    @POST("provider/signup")
    fun postSignup(
            @PartMap params: HashMap<String, RequestBody>,
            @Part fileName: MultipartBody.Part?
    ): Observable<SignupResponseModel>

    @FormUrlEncoded
    @POST("provider/password")
    fun postChangePassword(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<ChangePasswordResponseModel>

    @FormUrlEncoded
    @POST("provider/verify")
    fun verifyUser(@FieldMap params: HashMap<String, String>): Observable<VerifyUser>

    @GET("provider/profile")
    fun getProfile(@Header("Authorization") token: String): Observable<ProfileResponse>

    @FormUrlEncoded
    @POST("provider/signup")
    fun updateProfile(
            @FieldMap params: HashMap<String, RequestBody>,
            @Part fileName: MultipartBody.Part?
    ): Observable<CommonResponse>

    @GET("provider/adminservices")
    fun getServices(@Header("Authorization") token: String):
            Observable<ManageServicesResponseModel>

    @GET("provider/ridetype")
    fun getRides(@Header("Authorization") token: String): Observable<SetupRideResponseModel>

    @GET("provider/shoptype")
    fun getShops(@Header("Authorization") token: String): Observable<SetupShopResponseModel>

    @Multipart
    @POST("provider/vechile/add")
    fun postVehicle(
            @Header("Authorization") token: String,
            @PartMap params: HashMap<String, RequestBody>,
//            @Part vehicleImage: MultipartBody.Part,
            @Part rcBookMultipart: MultipartBody.Part,
            @Part insuranceMultipart: MultipartBody.Part
    ): Observable<AddVehicleResponseModel>
}