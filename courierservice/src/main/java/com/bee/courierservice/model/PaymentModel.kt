package com.bee.courierservice.model

data class PaymentModel(
        var statusCode: String,
        var title: String,
        var message: String,
//        var responseData:List<Any>,
        var error: List<Any>
)