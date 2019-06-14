package com.gox.base.repository

interface ApiListener {
    fun success(successData: Any)
    fun fail(failData: Throwable)
}