package com.xjek.xuberservice.model

data class Test(
        val category: Category? = Category(),
        val subcategory: Subcategory? = Subcategory()
) {
    data class Category(
            val company_id: Int? = 0,
            val id: Int? = 0,
            val picture: String? = "",
            val price_choose: String? = "",
            val service_category_name: String? = "",
            val service_category_order: Int? = 0,
            val service_category_status: Int? = 0
    )

    data class Subcategory(
            val company_id: Int? = 0,
            val id: Int? = 0,
            val picture: String? = "",
            val service_category_id: Int? = 0,
            val service_subcategory_name: String? = "",
            val service_subcategory_order: Int? = 0,
            val service_subcategory_status: Int? = 0
    )
}