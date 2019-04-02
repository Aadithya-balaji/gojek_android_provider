package com.xjek.provider.network

import com.xjek.provider.models.*
import com.xjek.user.data.repositary.remote.model.CountryListResponse
import io.reactivex.Observable
import retrofit2.http.*
import java.util.*
import retrofit2.http.Path
import kotlin.collections.HashMap


interface AppWebService {

    @FormUrlEncoded
    @POST("provider/login")
    fun postLogin(@FieldMap params: HashMap<String, String>): Observable<LoginResponseModel>

    @FormUrlEncoded
    @POST("provider/password")
    fun postChangePassword(
            @Header("Authorization") token: String,
            @FieldMap params: HashMap<String, String>
    ): Observable<ChangePasswordResponseModel>

    @FormUrlEncoded
    @POST("provider/signup")
    fun postSignup(@FieldMap params: HashMap<String, String>): Observable<SignupResponseModel>

    @FormUrlEncoded
    @POST("user/countries")
    fun getCountries(@FieldMap params:HashMap<String,Any?>):Observable<CountryListResponse>

    @GET("states/{state_id}")
    fun getStatelist(@Path("state_id") state_id: String): Observable<StateListResponse>

    @GET("cities/{city_id}")
    fun getCityList(@Path("city_id") city_id: String): Observable<CityListResponse>
}