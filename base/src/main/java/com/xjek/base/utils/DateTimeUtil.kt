package com.xjek.base.utils

import java.util.*

class DateTimeUtil {

    fun constructDateString(date: Date, separator: String): String {
        return StringBuilder().append(date.date)
                .append(separator)
                .append(date.month)
                .append(separator)
                .append(date.year).toString()
    }

    fun constructDateString(year: Int, month: Int, dayOfMonth: Int, separator: String): String {
        return StringBuilder().append(dayOfMonth)
                .append(separator)
                .append(month)
                .append(separator)
                .append(year).toString()
    }
}