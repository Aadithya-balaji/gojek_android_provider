package com.xjek.provider.utils

import com.xjek.provider.models.ConfigResponseModel.ResponseData.AppSetting.Language

object Constant {

    const val TAB_HOME = "Home"
    const val TAB_ORDER = "Order"
    const val TAB_NOTIFICATION = "Notification"
    const val TAB_ACCOUNT = "Account"

    const val M_TOKEN = "Bearer "
    const val APP_REQUEST_CODE = 99
    const val STATELIST_REQUEST_CODE = 101
    const val COUNTRYLIST_REQUEST_CODE = 100
    const val CITYLIST_REQUEST_CODE = 102
    const val COUNTRYCODE_PICKER_REQUEST_CODE = 111


    const val TEMP_FILE_NAME = "Gojek_Provider"
    lateinit var  privacyPolicyUrl:String
    const val CARD_ID = "card_1ENH9aG9bvHy22wUda2eyiFX"
    const val TYPE_PROVIDER = "provider"
    lateinit var languages :List<Language>
}