package com.xjek.base.network

import retrofit2.Response
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

class WebResponse<T> {

    val logger = Logger.getLogger(WebResponse::class.java.simpleName)

    val code: Int
    val body: T?
    val message: String?

    val isSuccessful: Boolean
        get() = code in 200..300

    val isFailure: Boolean

    constructor(error: Throwable) {
        this.code = 500
        this.body = null
        this.message = error.message
        this.isFailure = true
    }

    constructor(response: Response<T>) {
        this.code = response.code()

        if (response.isSuccessful) {
            this.body = response.body()
            this.message = null
            this.isFailure = false
        } else {
            var errorMessage: String? = null
            response.errorBody()?.let {
                try {
                    errorMessage = response.errorBody()!!.string()
                } catch (ignored: IOException) {
                    logger.log(Level.SEVERE, "error while parsing response", ignored)
                }
            }

            errorMessage?.apply {
                if (isNullOrEmpty() || trim { it <= ' ' }.isEmpty()) {
                    errorMessage = response.message()
                }
            }

            this.body = null
            this.message = errorMessage
            this.isFailure = true
        }
    }
}