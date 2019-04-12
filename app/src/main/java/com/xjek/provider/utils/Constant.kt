package com.xjek.provider.utils

import com.xjek.provider.models.ConfigResponseModel.ResponseData.AppSetting.Language

object Constant {

    const val TAB_HOME = "Home"
    const val TAB_ORDER = "Order"
    const val TAB_NOTIFICATION = "Notification"
    const val TAB_ACCOUNT = "Account"

    const val TEMP_FILE_NAME = "Gojek_Provider"
    lateinit var  privacyPolicyUrl:String
    const val CARD_ID = "card_1ENH9aG9bvHy22wUda2eyiFX"
    const val TYPE_PROVIDER = "provider"
    lateinit var languages :List<Language>
}