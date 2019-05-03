package com.xjek.provider.utils

import android.Manifest
import com.xjek.provider.BuildConfig
import com.xjek.provider.models.ConfigResponseModel.ResponseData.AppSetting.Language

object Constant {

    const val SERVICE_ID = "service_id"
    const val PROVIDER_VEHICLE = "provider_vehicle"
    const val PROVIDER_SERVICE = "provider_service"
    const val DOCUMENT_NAME = "document_name"
    const val DOCUMENT_TYPE = "document_type"
    const val DOCUMENT_ID = "document_id"
    const val IS_BACK_PAGE_REQUIRED = "is_back_page_required"
    const val DOCUMENT_FRONT_PAGE_URL = "document_front_page_url"
    const val DOCUMENT_BACK_PAGE_URL = "document_back_page_url"
    const val IS_EXPIRY_DATE_REQUIRED = "is_expiry_date_required"

    const val TAB_HOME = "Home"
    const val TAB_ORDER = "Order"
    const val TAB_NOTIFICATION = "Notification"
    const val TAB_ACCOUNT = "Account"

    const val M_TOKEN = "Bearer "
    const val APP_REQUEST_CODE = 99
    const val STATELIST_REQUEST_CODE = 101
    const val COUNTRYLIST_REQUEST_CODE = 100
    const val CITYLIST_REQUEST_CODE = 102
    const val PICK_FRONT_IMAGE = 103
    const val PICK_BACK_IMAGE = 104
    const val COUNTRYCODE_PICKER_REQUEST_CODE = 111

    const val TEMP_FILE_NAME = "Gojek_Provider"
    lateinit var privacyPolicyUrl: String
    const val TYPE_PROVIDER = "provider"
    lateinit var languages: List<Language>

}