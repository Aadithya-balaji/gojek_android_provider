package com.gox.partner.utils

import android.content.Context
import androidx.annotation.StringRes

class ResourceProvider(private val context: Context) {

    fun getContext(): Context = context

    fun getString(@StringRes stringId: Int, vararg formatArgs: Any): String =
            context.getString(stringId, *formatArgs)
}