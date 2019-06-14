package com.gox.base.utils

import android.location.Location

interface LocationCallBack {
    fun onSuccess(location: Location)
    fun onFailure(message: String)
}
