package com.gox.base.utils

import java.text.DecimalFormat
import java.util.*

class DateTimeUtil {

    fun constructDateString(date: Date, separator: String): String {
        return StringBuilder().append(date.date)
                .append(separator)
                .append(DecimalFormat("00").format(date.month + 1))
                .append(separator)
                .append(date.year).toString()
    }

    fun constructDateString(year: Int, month: Int, dayOfMonth: Int, separator: String): String {
        return StringBuilder().append(year)
                .append(separator)
                .append(DecimalFormat("00").format(month + 1))
                .append(separator)
                .append(dayOfMonth).toString()
    }
}