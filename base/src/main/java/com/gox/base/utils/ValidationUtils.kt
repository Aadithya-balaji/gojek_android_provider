package com.gox.base.utils

import android.util.Patterns

object ValidationUtils {

    fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isMinLength(input: String, minLength: Int) = minLength >= input.length

}