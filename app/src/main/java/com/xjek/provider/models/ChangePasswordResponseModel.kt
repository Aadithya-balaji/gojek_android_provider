package com.xjek.provider.models

import com.xjek.base.models.CommonResponse

data class ChangePasswordResponseModel(
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>,
        val responseData: List<Any>) : CommonResponse