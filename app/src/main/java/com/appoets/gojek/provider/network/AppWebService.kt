package com.appoets.gojek.provider.network

import com.appoets.gojek.provider.models.ChangePasswordResponseModel
import com.appoets.gojek.provider.models.LoginResponseModel
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.*

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
}