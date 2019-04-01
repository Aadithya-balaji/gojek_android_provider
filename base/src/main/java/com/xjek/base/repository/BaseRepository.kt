package com.xjek.base.repository

import com.xjek.base.base.BaseApplication
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
                .baseUrl(baseUrl)
                .build()
    }

    fun <T> createApiClient(service: Class<T>): T {
        return retrofit.create(service)
    }
}