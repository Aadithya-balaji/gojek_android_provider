package com.xjek.provider.utils

import com.xjek.provider.models.LanguageModel

object Constant {

    const val TAB_HOME = "Home"
    const val TAB_ORDER = "Order"
    const val TAB_NOTIFICATION = "Notification"
    const val TAB_ACCOUNT = "Account"

   // lateinit var baseUrl:String
    var baseUrl:String = "https://xjekapi.xtikr.com/api/v1"
    lateinit var accessToken: String
    const val TEMP_FILE_NAME = "Gojek_Provider"
    lateinit var  privacyPolicyUrl:String

    var languages = ArrayList<LanguageModel>()
}
