package com.xjek.provider.network

import com.xjek.provider.model.CountryListResponse
import com.xjek.provider.models.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
    fun getStateList(@Path("state_id") state_id: String): Observable<StateListResponse>

    @GET("cities/{city_id}")
    fun getCityList(@Path("city_id") city_id: String): Observable<CityListResponse>

    @Multipart
    @POST("provider/signup")
    fun postSignUp(
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

    @Multipart
    @POST("provider/profile")
    fun profileUpdate(
            @Header("Authorization") token: String,
            @PartMap params: java.util.HashMap<String, RequestBody>,
            @Part image: MultipartBody.Part
    ): Observable<ResProfileUpdate>


    @FormUrlEncoded
    @POST("provider/signup")
    fun updateProfile(
            @FieldMap params: HashMap<String, RequestBody>,
            @Part fileName: MultipartBody.Part?
    ): Observable<CommonResponse>

    @GET("provider/card")
    fun getCardList(
            @Header("Authorization") token: String,
            @Query("limit") limit: String,
            @Query("offset") offset: String
    ): Observable<CardListModel>

    @FormUrlEncoded
    @POST("provider/add/money")
    fun addWalletMoney(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<WalletResponse>

    @GET("provider/wallet")
    fun getWalletTransction(@Header("Authorization") token: String): Observable<WalletTransactionList>

    @FormUrlEncoded
    @POST("provider/card")
    fun addCard(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<AddCardModel>

    @DELETE("provider/card/{card_id}")
    fun deleteCard(
            @Header("Authorization") token: String,
            @Path("card_id") cardid: String
    ): Observable<AddCardModel>

    @FormUrlEncoded
    @POST
    fun changeAvailability(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<ResponseBody>

    @GET("provider/check/request")
    fun getRequest(
            @Header("Authorization") token: String,
            @Query("latitude") lat: String,
            @Query("longitude") lon: String
    ): Observable<CheckRequestModel>

    @GET("provider/notification")
    fun getNotification(@Header("Authorization") token: String): Observable<NotificationResponse>

    @GET("provider/onlinestatus/{status_id}")
    fun changeOnlineStatus(@Header("Authorization") token: String,@Path("status_id") statusID: String): Observable<StatusResponseModel>
    @FormUrlEncoded
    @POST("provider/accept/request")
    fun acceptIncomingRequest(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<AcceptRequestModel>

    @FormUrlEncoded
    @POST("provider/cancel/request")
    fun rejectIncomingRequest(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<RejectRequestModel>


}