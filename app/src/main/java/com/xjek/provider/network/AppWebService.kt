package com.xjek.provider.network

import com.xjek.provider.model.*
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
    fun changeOnlineStatus(@Header("Authorization") token: String, @Path("status_id") statusID: String): Observable<StatusResponseModel>

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

    fun updateProfile(
            @FieldMap params: HashMap<String, RequestBody>,
            @Part fileName: MultipartBody.Part?
    ): Observable<CommonResponse>

    @GET("provider/adminservices")
    fun getServices(@Header("Authorization") token: String):
            Observable<ManageServicesResponseModel>

    @GET("provider/ridetype")
    fun getRides(@Header("Authorization") token: String): Observable<SetupRideResponseModel>

    @GET("provider/providerservice/categories")
    fun getServiceCategories(@Header("Authorization") token: String): Observable<ServiceCategoriesResponse>

    @FormUrlEncoded
    @POST("provider/providerservice/subcategories")
    fun getSubServiceCategories(@Header("Authorization") token: String, @FieldMap params: HashMap<String, String>): Observable<SubServiceCategoriesResponse>

    @FormUrlEncoded
    @POST("provider/providerservice/service")
    fun getSubServicePriceCategories(@Header("Authorization") token: String, @FieldMap params: HashMap<String, String>): Observable<SubServicePriceCategoriesResponse>

    @GET("provider/shoptype")
    fun getShops(@Header("Authorization") token: String): Observable<SetupShopResponseModel>

    @GET("provider/services/list")
    fun getVehicleCategories(@Header("Authorization") token: String):
            Observable<VehicleCategoryResponseModel>

    @Multipart
    @POST("provider/vechile/add")
    fun postVehicle(
            @PartMap params: HashMap<String, RequestBody>,
            @Part vehicleImage: MultipartBody.Part?,
            @Part rcBookMultipart: MultipartBody.Part?,
            @Part insuranceMultipart: MultipartBody.Part?
    ): Observable<AddVehicleResponseModel>


    @Multipart
    @POST("provider/vehicle/edit")
    fun editVehicle(
            @PartMap params: HashMap<String, RequestBody>,
            @Part vehicleImage: MultipartBody.Part?,
            @Part rcBookMultipart: MultipartBody.Part?,
            @Part insuranceMultipart: MultipartBody.Part?
    ): Observable<AddVehicleResponseModel>

    @FormUrlEncoded
    @POST("provider/vehicle/edit")
    fun postVehicle(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<AddVehicleResponseModel>

    @FormUrlEncoded
    @POST("provider/listdocuments")
    fun getDocuments(@Field("type") documentType: String
    ): Observable<ListDocumentResponse>


    @GET("provider/bankdetails/template")
    fun getBankTemplate(@Header("Authorization") token: String): Observable<BankTemplateModel>

    @GET("provider/providerservice/categories")
    fun getCategories(@Header("Authorization") token: String): Observable<CategoriesResponseModel>

    @Headers("Content-Type: application/json")
    @POST("provider/addbankdetails")
    fun postAddBankDetails(@Header("Authorization") token: String, @Body body: String): Observable<AddBankDetailsModel>

    @Headers("Content-Type: application/json")
    @POST("provider/editbankdetails")
    fun postEditBankDetails(@Header("Authorization") token: String, @Body body: String): Observable<AddBankDetailsModel>

    @Multipart
    @POST("provider/documents")
    fun postDocument(@PartMap params: java.util.HashMap<String, RequestBody>,
                     @Part frontImage: MultipartBody.Part?, @Part backImage: MultipartBody.Part?
    ): Observable<AddDocumentResponse>

    @GET("provider/history/{selcetedservice}")
    fun getTransportHistory(@Header("Authorization") token: String,
                            @Path("selcetedservice") selectedservice: String,
                            @QueryMap params: HashMap<String, String>): Observable<TransportHistory>

    @GET("provider/history/{selcetedservice}")
    fun getPastHistory(@Header("Authorization") token: String,
                       @Path("selcetedservice") selectedservice: String,
                       @QueryMap params: HashMap<String, String>): Observable<HistoryModel>

    @GET("provider/upcoming/trips/transport")
    fun getServiceHistory(@Header("Authorization") token: String,
                          @QueryMap params: HashMap<String, String>): Observable<TransportHistory>


    @GET("provider/history/transport/{id}")
    fun getHistoryDetail(@Header("Authorization") token: String,
                         @Path("id") id: String): Observable<HistoryDetailModel>

    @GET("provider/history/transport/{id}")
    fun getUpcomingHistoryDetail(@Header("Authorization") token: String,
                                 @Path("id") id: String): Observable<HistoryDetailModel>

    @GET("provider/ride/dispute")
    fun getDisputeList(@Header("Authorization") token: String): Observable<DisputeListModel>

    @FormUrlEncoded
    @POST("provider/ride/dispute")
    abstract fun addDispute(@Header("Authorization") token: String,
                            @FieldMap params: HashMap<String, String>): Observable<ResponseData>

    @GET("provider/ride/dispute/{id}")
    fun getDisputeStatus(@Header("Authorization") token: String,
                         @Path("id") id: String): Observable<DisputeStatusModel>

    @GET("provider/earnings/{id}")
    fun getEarnings(
            @Header("Authorization") token: String,
            @Path("id") id: Int
    ): Observable<EarningsResponse>
}
