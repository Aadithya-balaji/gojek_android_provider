package com.gox.base.models

interface CommonResponse {
    val statusCode: String
    val title: String
    val message: String
    val error: List<Any>
}