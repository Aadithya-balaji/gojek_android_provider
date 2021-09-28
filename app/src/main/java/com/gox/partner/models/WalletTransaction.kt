package com.gox.partner.models

import com.google.gson.annotations.SerializedName

data class WalletTransaction(
    val error: List<Any> = listOf(),
    val message: String = "",
    val responseData: ResponseData = ResponseData(),
    val statusCode: String = "",
    val title: String = "",
) {
    data class ResponseData(
        val data: List<Data> = listOf(),
        val current_page: Int = 0,
        val first_page_url: String = "",
        val from: Int = 0,
        val last_page: Int = 0,
        val last_page_url: String = "",
        val next_page_url: Any? = Any(),
        val path: String = "",
        val per_page: Int = 0,
        val prev_page_url: Any? = Any(),
        val to: Int = 0,
        val total: Int = 0,
    ) {
        data class Data(
            val amount: Double = 0.0,
            val created_at: String = "",
            val id: Int = 0,
            val payment_log: PaymentLog = PaymentLog(),
            val provider: Provider = Provider(),
            val provider_id: Int = 0,
            val transaction_alias: String = "",
            val transaction_desc: String = "",
            val transaction_id: Int = 0,
            val type: String = "",
            val transactions: List<TransactionFormat> = listOf(),
        ) {
            data class PaymentLog(
                val amount: Double = 0.0,
                val company_id: Int = 0,
                val id: Int = 0,
                val is_wallet: Int = 0,
                val payment_mode: String = "",
                val transaction_code: String = "",
                val user_id: Int = 0,
                val user_type: String = "",
            )

            data class TransactionFormat(
                var admin_service: String = "",
                var amount: Double = 0.0,
                var close_balance: Double = 0.0,
                var company_id: Int = 0,
                var created_at: String = "",
                var created_time: String = "",
                var id: Int = 0,
                var open_balance: Double = 0.0,
                var provider_id: Int = 0,
                var transaction_alias: String = "",
                var transaction_desc: String = "",
                var transaction_id: Int = 0,
                var type: String = "",
            )

            data class Provider(
                val currency_symbol: String = "",
                val id: Int = 0,
            )
        }
    }
}