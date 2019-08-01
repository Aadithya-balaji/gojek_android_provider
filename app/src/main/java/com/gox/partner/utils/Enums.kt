package com.gox.partner.utils

import androidx.annotation.IntDef
import androidx.annotation.StringDef

class Enums {

    companion object {

        const val DOC_TAXI: Int = 0
        const val DOC_FOODIE: Int = 1
        const val DOC_SERVICE: Int = 2

        const val DOC_TYPE_LICENSE: Int = 1
        const val DOC_TYPE_BIRTH: Int = 2

        //Common Values
        const val DEVICE_TYPE: String = "ANDROID"
        const val MANUAL: String = "MANUAL"
        const val GOOGLE: String = "GOOGLE"
        const val FACEBOOK: String = "FACEBOOK"

        //Acitivity Request Code
        const val FB_ACCOUNT_KIT_CODE: Int = 122
        const val GOOGLE_REQ_CODE: Int = 123
        const val RC_COUNTRY_CODE_PICKER = 100
        const val RC_GOOGLE_SIGN_IN = 101
        const val COUNTRYLIST_REQUEST_CODE = 124
        const val CITYLIST_REQUEST_CODE = 125
        const val FILE_REQ_CODE = 126
        const val LOCATION_REQUEST_CODE = 126
        const val RC_VEHICLE_IMAGE = 102
        const val RC_RC_BOOK_IMAGE = 103
        const val RC_INSURANCE_IMAGE = 104

        const val DOCUMENT_UPLOAD_FRONT = 106
        const val DOCUMENT_UPLOAD_BACK = 107

        const val DOCUMENT_UPLOAD_PDF = 108


        const val IMAGE_TYPE = "image"
        const val PDF_TYPE = "pdf"

        const val PDF_EXTENSION = ".pdf"

        //PaymentType
        const val PAYMENT_CASH = 1
        const val PAYMENT_CARD = 2
        const val PAYMENT_PAYAPL = 3
        const val PAYMENT_PAYTM = 4
        const val PAYMENT_PAYUMONEY = 5
        const val PAYMENT_BRAINTREE = 6

        //Dialoge Type
        const val PAYMENT_PENDING = 11
        const val ADMIN_APPROVAL = 12
        const val LOW_BALANCE = 13
    }

    @IntDef(DOC_TAXI, DOC_FOODIE, DOC_SERVICE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ServiceType

    @IntDef(DOC_TYPE_LICENSE, DOC_TYPE_BIRTH)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class DocumentType

    @StringDef(DEVICE_TYPE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class CommonData

    @IntDef(FB_ACCOUNT_KIT_CODE, GOOGLE_REQ_CODE, RC_COUNTRY_CODE_PICKER, RC_GOOGLE_SIGN_IN, COUNTRYLIST_REQUEST_CODE, CITYLIST_REQUEST_CODE, FILE_REQ_CODE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class OnActivityResultCode

    @IntDef(PAYMENT_PENDING, ADMIN_APPROVAL, LOW_BALANCE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class DialogeType

    enum class LOGIN_BY {
        MANUAL, FACEBOOK, GOOGLE
    }

}
