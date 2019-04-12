package com.xjek.provider.utils

import com.xjek.provider.models.LanguageModel

object Constant {

    const val SERVICE_ID = "service_id"
    const val PROVIDER_VEHICLE = "provider_vehicle"

    const val TAB_HOME = "Home"
    const val TAB_ORDER = "Order"
    const val TAB_NOTIFICATION = "Notification"
    const val TAB_ACCOUNT = "Account"

    const val TEMP_FILE_NAME = "Gojek_Provider"
    lateinit var privacyPolicyUrl: String

    var languages = ArrayList<LanguageModel>()
}
