package com.xjek.provider.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CmspageModel {
    @SerializedName("privacypolicy")
    @Expose
    var privacypolicy: String? = null
    @SerializedName("help")
    @Expose
    var help: String? = null
    @SerializedName("terms")
    @Expose
    var terms: String? = null
    @SerializedName("cancel")
    @Expose
    var cancel: String? = null
}
