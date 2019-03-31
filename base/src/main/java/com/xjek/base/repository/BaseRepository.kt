package com.xjek.base.repository

import com.xjek.base.base.BaseApplication
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
}