package com.gox.partner.views.incoming_request_taxi

interface IncomingNavigator {
    fun accept()
    fun cancel()
    fun showErrorMessage(error: String)
}