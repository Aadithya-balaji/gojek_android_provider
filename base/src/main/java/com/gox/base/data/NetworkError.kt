package com.gox.base.data

object NetworkError {
    const val TIME_OUT = "Network Timeout. Retry again!"
    const val IO_EXCEPTION = "Internal Server Error. Retry later"
    const val SERVER_EXCEPTION = "Something Went Wrong On Server. Please try later"
    const val DATA_EXCEPTION = "Data parsing issue"
    const val UNKNOWN_HOST_EXCEPTION = "Please check you internet connection!!."
}