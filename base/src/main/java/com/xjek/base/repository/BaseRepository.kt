package com.xjek.base.repository

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.xjek.base.base.BaseApplication
import com.xjek.base.data.NetworkError
import com.xjek.base.data.PreferencesHelper
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

open class BaseRepository {

    @Inject
    lateinit var retrofit: Retrofit

    init {
        BaseApplication().baseComponent.inject(this)
    }

    fun <T> createApiClient(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun <T> createApiClient(serviceId: String, service: Class<T>): T {
        return reconstructedRetrofit(serviceId).create(service)
    }

    private fun reconstructedRetrofit(serviceId: String): Retrofit {
        return retrofit.newBuilder()
                .baseUrl(StringBuilder(PreferencesHelper.get<String>(serviceId))
                        .append("/").toString())
                .build()
    }


    fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                val responseBody = e.response().errorBody()
                if (e.code() == 401){

                }
                getErrorMessage(responseBody!!)
            }
            is SocketTimeoutException -> NetworkError.TIME_OUT
            is IOException -> NetworkError.IO_EXCEPTION
            else -> {
                NetworkError.SERVER_EXCEPTION
            }
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody): String {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message!!
        }
    }
}