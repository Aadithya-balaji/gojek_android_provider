package com.xjek.provider.models

data class Test(
        val dispute: Dispute? = Dispute()
) {
    data class Dispute(
            val comments: Any? = Any(),
            val comments_by: String? = "",
            val company_id: Int? = 0,
            val created_at: String? = "",
            val dispute_name: String? = "",
            val dispute_title: Any? = Any(),
            val dispute_type: String? = "",
            val id: Int? = 0,
            val is_admin: Int? = 0,
            val provider_id: Int? = 0,
            val refund_amount: Int? = 0,
            val service_request_id: Int? = 0,
            val status: String? = "",
            val user_id: Int? = 0
    )
}