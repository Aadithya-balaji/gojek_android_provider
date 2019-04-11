package com.xjek.base.repository

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.xjek.base.base.BaseApplication
import com.xjek.base.data.PreferencesHelper
import org.json.JSONObject
import retrofit2.Retrofit
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
                .baseUrl(PreferencesHelper.get<String>(serviceId))
                .build()
    }

    fun getErrorMessage(e: Throwable): String {
        val errorObject = JSONObject((e as HttpException).response().errorBody()?.string())
        return if (errorObject.has("message"))
            errorObject.getString("message")
        else
            e.message()
    }
}