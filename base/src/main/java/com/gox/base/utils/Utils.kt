package com.gox.base.utils

import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import java.text.DecimalFormat
import java.text.NumberFormat
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

    fun getNumberFormat(): NumberFormat? {
        val currency: String = PreferencesHelper.get(PreferencesKey.CURRENCY_SYMBOL,"$")?:"$"
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val decimalFormatSymbols = (numberFormat as DecimalFormat).decimalFormatSymbols
        decimalFormatSymbols.currencySymbol = currency
        numberFormat.decimalFormatSymbols = decimalFormatSymbols
        numberFormat.setMinimumFractionDigits(2)
        return numberFormat
    }

}