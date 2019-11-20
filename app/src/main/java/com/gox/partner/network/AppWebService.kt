package com.gox.partner.network

import com.gox.base.chatmessage.ChatMessageList
import com.gox.partner.models.*
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

    @Multipart
    @POST("user/send-otp")
    fun sendOTP(@PartMap params: HashMap<String, RequestBody>): Observable<SendOTPResponse>


    @Multipart
    @POST("user/verify-otp")
    fun verifyOTP(@PartMap params: HashMap<String, RequestBody>): Observable<VerifyOTPResponse>

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
    fun getCountries(@FieldMap params: HashMap<String, Any?>):
            Observable<CountryListResponse>

    @Multipart
    @POST("provider/signup")
    fun postSignUp(
            @PartMap params: HashMap<String, RequestBody>,
            @Part fileName: MultipartBody.Part?
    ): Observable<RegistrationResponseModel>


    @FormUrlEncoded
    @POST("provider/password")
    fun postChangePassword(
            @FieldMap params: HashMap<String, String>
    ): Observable<ChangePasswordResponseModel>

    @FormUrlEncoded
    @POST("provider/verify")
    fun verifyUser(@FieldMap params: HashMap<String, String>):
            Observable<VerifyUser>

    @GET("provider/profile")
    fun getProfile(): Observable<ProfileResponse>

    @Multipart
    @POST("provider/profile")
    fun profileUpdate(
            @PartMap params: java.util.HashMap<String, RequestBody>,
            @Part image: MultipartBody.Part?
    ): Observable<ResProfileUpdate>

    @GET("provider/card")
    fun getCardList(
            @Query("limit") limit: String,
            @Query("offset") offset: String
    ): Observable<CardListModel>

    @FormUrlEncoded
    @POST("provider/add/money")
    fun addWalletMoney(
            @FieldMap params: HashMap<String, String>
    ): Observable<WalletResponse>

    @GET("provider/wallet")
    fun getWalletTransction(): Observable<WalletTransaction>

    @FormUrlEncoded
    @POST("provider/card")
    fun addCard(
            @FieldMap params: HashMap<String, String>
    ): Observable<AddCardModel>

    @DELETE("provider/card/{card_id}")
    fun deleteCard(
            @Path("card_id") cardid: String
    ): Observable<AddCardModel>

    @GET("provider/check/request")
    fun getRequest(
            @Query("latitude") lat: String,
            @Query("longitude") lon: String
    ): Observable<CheckRequestModel>

    @GET("provider/notification")
    fun getNotification(): Observable<NotificationResponse>

    @GET("provider/onlinestatus/{status_id}")
    fun changeOnlineStatus(
            @Path("status_id") statusID: String):
            Observable<StatusResponseModel>

    @FormUrlEncoded
    @POST("provider/accept/request")
    fun acceptIncomingRequest(
            @FieldMap params: HashMap<String, String>
    ): Observable<AcceptRequestModel>

    @FormUrlEncoded
    @POST("provider/cancel/request")
    fun rejectIncomingRequest(
            @FieldMap params: HashMap<String, String>
    ): Observable<RejectRequestModel>

    @GET("provider/adminservices")
    fun getServices(): Observable<ManageServicesResponseModel>

    @GET("provider/ridetype")
    fun getRides(): Observable<SetupRideResponseModel>

    @GET("provider/providerservice/categories")
    fun getServiceCategories(): Observable<ServiceCategoriesResponse>

    @FormUrlEncoded
    @POST("provider/providerservice/subcategories")
    fun getSubServiceCategories(@FieldMap params: HashMap<String, String>):
            Observable<SubServiceCategoriesResponse>

    @FormUrlEncoded
    @POST("provider/providerservice/service")
    fun getSubServicePriceCategories(@FieldMap params: HashMap<String, String>):
            Observable<SubServicePriceCategoriesResponse>

    @GET("provider/shoptype")
    fun getShops(): Observable<SetupShopResponseModel>

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
    @POST("provider/vechile/add")
    fun postVehicle(
            @FieldMap params: HashMap<String, String>
    ): Observable<AddVehicleResponseModel>


    @FormUrlEncoded
    @POST("provider/vehicle/edit")
    fun editVehicle(
            @FieldMap params: HashMap<String, String>
    ): Observable<AddVehicleResponseModel>

    @FormUrlEncoded
    @POST("provider/listdocuments")
    fun getDocuments(@Field("type") documentType: String
    ): Observable<ListDocumentResponse>

    @GET("provider/bankdetails/template")
    fun getBankTemplate(): Observable<BankTemplateModel>

    @Headers("Content-Type: application/json")
    @POST("provider/addbankdetails")
    fun postAddBankDetails(@Body body: String): Observable<AddBankDetailsModel>

    @Headers("Content-Type: application/json")
    @POST("provider/editbankdetails")
    fun postEditBankDetails(@Body body: String): Observable<AddBankDetailsModel>

    @Multipart
    @POST("provider/documents")
    fun postDocument(@PartMap params: java.util.HashMap<String, RequestBody>,
                     @Part frontImage: MultipartBody.Part?,
                     @Part backImage: MultipartBody.Part?
    ): Observable<AddDocumentResponse>

    @GET("provider/history/{selcetedservice}")
    fun getTransportHistory(
            @Path("selcetedservice") selectedService: String,
            @QueryMap params: HashMap<String, String>
    ): Observable<TransportHistory>

    @GET("provider/history/{selcetedservice}")
    fun getPastHistory(
            @Path("selcetedservice") selectedService: String,
            @QueryMap params: HashMap<String, String>
    ): Observable<HistoryModel>

    @GET("provider/history/transport/{id}")
    fun getHistoryDetail(@Path("id") id: String): Observable<HistoryDetailModel>

    @GET("provider/history/service/{id}")
    fun getServiceDetail(@Path("id") id: String): Observable<HistoryDetailModel>

    @GET("provider/history/order/{id}")
    fun getOrderDetail(@Path("id") id: String): Observable<HistoryDetailModel>

    @GET("provider/ride/dispute")
    fun getDisputeList(): Observable<DisputeListModel>

    @GET("provider/{service}/dispute")
    fun getDisputeReasons(@Path("service") servicetype: String): Observable<DisputeListModel>

    @FormUrlEncoded
    @POST("provider/history-dispute/transport")
    fun postTaxiDispute(@FieldMap params: HashMap<String, String>): Observable<DisputeStatus>

    @FormUrlEncoded
    @POST("provider/history-dispute/service")
    fun postServiceDispute(
            @FieldMap params: HashMap<String, String>
    ): Observable<DisputeStatus>

    @FormUrlEncoded
    @POST("provider/history-dispute/order")
    fun postOrderDispute(@FieldMap params: HashMap<String, String>): Observable<DisputeStatus>

    @GET("provider/earnings/{id}")
    fun getEarnings(@Path("id") id: Int): Observable<EarningsResponse>

    @GET("provider/ride/disputestatus/{request_id}")
    fun getTransportDisputeStatus(@Path("request_id") id: String): Observable<DisputeStatusModel>

    @GET("provider/order/disputestatus/{request_id}")
    fun getOrderDisputeStatus(@Path("request_id") id: String): Observable<DisputeStatusModel>

    @GET("provider/service/disputestatus/{request_id}")
    fun getServiceDisputeStatus(@Path("request_id") id:String):Observable<DisputeStatusModel>

    @POST("provider/logout")
    fun logout(): Observable<ResponseData>

}