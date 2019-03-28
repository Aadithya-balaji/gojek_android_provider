@file:JvmName("Converter")

package com.appoets.base.utils

import androidx.databinding.InverseMethod

@InverseMethod("convertStringToInt")
fun convertIntToString(value: Int): String {
    return value.toString()
}

fun convertStringToInt(value: String): Int {
    return try {
        Integer.parseInt(value)
    } catch (e: NumberFormatException) {
        -1
    }
}