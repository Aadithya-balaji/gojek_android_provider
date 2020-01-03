package com.gox.base.repository

import com.google.gson.JsonSyntaxException
import com.gox.base.BuildConfig
import com.gox.base.base.BaseApplication
import com.gox.base.data.NetworkError
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.session.SessionManager
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

open class BaseRepository {

    @Inject
    lateinit var retrofit: Retrofit

    init {
        BaseApplication().baseComponent.inject(this)
    }

    fun <T> createApiClient(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun <T> createApiClient(baseUrl: String, service: Class<T>): T {
        return reconstructedRetrofit(baseUrl).create(service)
    }

    @Singleton
    fun reconstructedRetrofit(baseUrl: String): Retrofit {
        return retrofit.newBuilder()
                .baseUrl("$baseUrl/")
                .build()
    }

    fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is JsonSyntaxException -> {
                if (BuildConfig.DEBUG) e.message.toString()
                else NetworkError.DATA_EXCEPTION
            }
            is HttpException -> {
                if (e.code() == 401 && !PreferencesHelper
                                .get(PreferencesKey.ACCESS_TOKEN, "")
                                .equals(""))
                    SessionManager.clearSession()
                getErrorMessage(e.response().errorBody()!!)
            }
            is UnknownHostException ->NetworkError.UNKNOWN_HOST_EXCEPTION
            is ConnectException ->NetworkError.UNKNOWN_HOST_EXCEPTION
            is SocketTimeoutException -> NetworkError.TIME_OUT
            is IOException -> NetworkError.IO_EXCEPTION
            else -> NetworkError.SERVER_EXCEPTION
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