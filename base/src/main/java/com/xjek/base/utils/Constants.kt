package com.xjek.base.utils

import java.text.SimpleDateFormat
import java.util.*

class  Constants{
    companion object {
        val APPNAME:String="Gojek"
        val UTCTIME="YYYY-DD-MM  HH:mm:ss"
        val SERVICESIMPLEDATEFORMAT = SimpleDateFormat(UTCTIME, Locale.getDefault())

        val currency = "$"

    }
}