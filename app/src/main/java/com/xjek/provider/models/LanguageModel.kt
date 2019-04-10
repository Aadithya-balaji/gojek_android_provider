package com.xjek.provider.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LanguageModel {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("key")
    @Expose
    var key: String? = null
}
