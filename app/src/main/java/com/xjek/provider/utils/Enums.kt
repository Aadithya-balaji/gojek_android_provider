package com.xjek.provider.utils


import androidx.annotation.IntDef
import androidx.annotation.StringDef

class Enums {
companion object {

    const val DOC_TAXI: Int = 0
    const val DOC_FOODIE: Int = 1
    const val DOC_SERVICE: Int = 2

    const  val DOC_TYPE_LICENSE: Int = 1
    const val DOC_TYPE_BIRTH: Int = 2

    //Common Values
    const val DEVICE_TYPE: String = "ANDROID"
    const val MANUAL: String = "MANUAL"
    const val GOOGLE: String = "GOOGLE"
    const val FACEBOOK: String = "FACEBOOK"


    //Acitivity Request Code
    const val FB_ACCOUNT_KIT_CODE:Int=122
    const val GOOGLE_REQ_CODE:Int=123

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



    @IntDef(FB_ACCOUNT_KIT_CODE, GOOGLE_REQ_CODE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class  OnActivityResultCode

    enum class  LOGINBY{
        MANUAL,FACEBOOK,GOOGLE
    }


}
