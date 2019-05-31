package com.gox.base.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {


    fun parseDateToYYYYMMdd(time: String?): String? {
        if(time.isNullOrEmpty()) {
            return ""
        }
        val inputPattern = "yyyy-MM-dd HH:mm:ss"
        val outputPattern = "yyyy-MM-dd"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }



}