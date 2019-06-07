package com.gox.base.chatmessage

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ReqChatModel : Serializable {
    @SerializedName("room")
    var roomName: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("user")
    var userFirstName: String? = null
    @SerializedName("provider")
    var providerFirstName: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("admin_service_id")
    var adminServiceId: Int? = null
    @SerializedName("admin_service")
    var adminService: String? = null
    @SerializedName("request_id")
    var requestId: Int? = null
}