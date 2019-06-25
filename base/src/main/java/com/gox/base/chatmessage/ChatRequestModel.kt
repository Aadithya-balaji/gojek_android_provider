package com.gox.base.chatmessage

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ChatRequestModel : Serializable {
    @SerializedName("room")
    var roomName: String = ""
    @SerializedName("url")
    var url: String = ""
    @SerializedName("salt_key")
    var saltKey: String = ""
    @SerializedName("id")
    var requestId: Int = 0
    @SerializedName("admin_service")
    var adminService: String = ""

    @SerializedName("type")
    var type: String = ""
    @SerializedName("user")
    var userName: String = ""
    @SerializedName("provider")
    var providerName: String = ""
    @SerializedName("message")
    var message: String = ""
}