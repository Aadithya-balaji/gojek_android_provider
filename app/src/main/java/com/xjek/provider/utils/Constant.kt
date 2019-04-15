package com.xjek.provider.utils

import com.xjek.provider.models.LanguageModel

object Constant {

    const val SERVICE_ID = "service_id"
    const val PROVIDER_VEHICLE = "provider_vehicle"
    const val DOCUMENT_NAME = "document_name"
    const val DOCUMENT_ID = "document_id"
    const val IS_BACK_PAGE_REQUIRED = "is_back_page_required"
    const val DOCUMENT_FRONT_PAGE_URL = "document_front_page_url"
    const val DOCUMENT_BACK_PAGE_URL = "document_back_page_url"
    const val IS_EXPIRY_DATE_REQUIRED = "is_expiry_date_required"

    const val TAB_HOME = "Home"
    const val TAB_ORDER = "Order"
    const val TAB_NOTIFICATION = "Notification"
    const val TAB_ACCOUNT = "Account"

    const val TEMP_FILE_NAME = "Gojek_Provider"
    lateinit var privacyPolicyUrl: String

    var languages = ArrayList<LanguageModel>()
}
