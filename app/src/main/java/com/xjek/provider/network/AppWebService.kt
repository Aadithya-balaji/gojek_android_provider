package com.xjek.provider.network

import com.xjek.provider.models.*
import io.reactivex.Observable
import retrofit2.http.*
import java.util.*
import retrofit2.http.Path


interface AppWebService {

    @FormUrlEncoded
    @POST("provider/login")
    fun postLogin(@FieldMap params: HashMap<String, String>): Observable<LoginResponseModel>

    @FormUrlEncoded
    @POST("provider/signup")
    fun postSignup(@FieldMap params: HashMap<String, String>): Observable<SignupResponseModel>

    @GET("countries/{country_id}")
    fun getCountries(@Path("country_id") country_id: String): Observable<CountryListResponse>

    @GET("states/{state_id}")
    fun getStatelist(@Path("state_id") state_id: String): Observable<StateListResponse>

    @GET("cities/{city_id}")
    fun getCityList(@Path("city_id") city_id: String): Observable<CityListResponse>
}