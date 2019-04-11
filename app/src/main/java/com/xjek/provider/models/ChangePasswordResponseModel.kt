package com.xjek.provider.models

import com.xjek.base.models.CommonResponse

data class ChangePasswordResponseModel(
        val responseData: List<Any>,
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>
) : CommonResponse