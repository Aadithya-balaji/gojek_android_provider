package com.xjek.provider.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ContactNumberModel {
    @SerializedName("number")
    @Expose
    var number: String? = null
}
