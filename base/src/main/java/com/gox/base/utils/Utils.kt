package com.gox.base.utils

import android.widget.TextView
import androidx.annotation.NonNull
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val inputPattern = "yyyy-MM-dd HH:mm:ss"
    private const val outputPattern = "yyyy-MM-dd"
    private const val userDateFormatPattern = "dd-MM-yyyy"

    fun parseDateToYYYYMMdd(time: String?): String? {
        if (time.isNullOrEmpty()) {
            return ""
        }
        val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
        val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())

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

    fun dateHasExpired(checkDate: String): Boolean{
        return SimpleDateFormat(inputPattern, Locale.getDefault()).parse(checkDate).before(Date())
    }

    fun userDateFormat(resDate: String): String{
        var formattedDate =""
        val date =SimpleDateFormat(inputPattern,Locale.getDefault()).parse(resDate)
        val userFormat =SimpleDateFormat(userDateFormatPattern,Locale.getDefault())
        formattedDate= userFormat.format(date)
        return formattedDate
    }

    fun getNumberFormat(): NumberFormat? {
        val currency: String = PreferencesHelper.get(PreferencesKey.CURRENCY_SYMBOL, "$") ?: "$"
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val decimalFormatSymbols = (numberFormat as DecimalFormat).decimalFormatSymbols
        decimalFormatSymbols.currencySymbol = currency
        numberFormat.decimalFormatSymbols = decimalFormatSymbols
        numberFormat.setMinimumFractionDigits(2)
        return numberFormat
    }

    fun capitalize(@NonNull input: String): String? {
        val words = input.toLowerCase(Locale.getDefault()).split(" ").toTypedArray()
        val builder = StringBuilder()
        for (i in words.indices) {
            val word = words[i]
            if (i > 0 && word.isNotEmpty()) {
                builder.append(" ")
            }
            val cap = word.substring(0, 1).toUpperCase(Locale.getDefault()) + word.substring(1)
            builder.append(cap)
        }
        return builder.toString()
    }

}