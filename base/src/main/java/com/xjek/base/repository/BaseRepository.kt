package com.xjek.base.repository

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.xjek.base.base.BaseApplication
import org.json.JSONObject
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

open class BaseRepository {

    @Inject
    lateinit var retrofit: Retrofit

    init {
        BaseApplication().baseComponent.inject(this)
    }

    @Singleton
    fun reconstructedRetrofit(baseUrl: String): Retrofit {
        return retrofit.newBuilder()
                .baseUrl("$baseUrl/")
                .build()
    }

    fun <T> createApiClient(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun <T> createApiClient(baseUrl: String, service: Class<T>): T {
        return reconstructedRetrofit(baseUrl).create(service)
    }


    fun getErrorMessage(e: Throwable): String {
        val errorObject = JSONObject((e as HttpException).response().errorBody()?.string())
        return if (errorObject.has("message"))
            errorObject.getString("message")
        else
            e.message()
    }
}